package com.careplus.server.net;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;
import com.careplus.server.service.AuthService;
import com.careplus.server.service.PaymentService;

/*
 * ClientHandler
 * One instance per connected client, each running on its own thread.
 *
 * This is the server side entry point of the wire protocol: it reads Request
 * objects in a loop, routes them to a service by RequestType, and writes back a
 * Response. Because every client gets a dedicated instance, nothing in this class
 * is shared across connections, and the services it owns hold per request state
 * only. The single piece of genuinely shared state in the request path is the
 * Hibernate SessionFactory reached through HibernateUtil.
 */
public class ClientHandler extends Thread {
	private Socket socket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;

	/*
	 * Services are instantiated per handler rather than shared or static, which is
	 * what keeps BaseService's session and transaction fields safe. Those fields are
	 * plain instance state with no locking, so a single shared service instance
	 * across client threads would let one request commit or close another's
	 * transaction.
	 */
	private AuthService authservice;
	private PaymentService paymentService;

	private static final Logger logger = LogManager.getLogger(Server.class);
	
	public ClientHandler(Socket socket) {
		this.socket = socket;
		authservice = new AuthService();
		paymentService = new PaymentService();

	}

	private void getStreams() {
		try {
			/*
			 * Output before input, mirroring the client. Both ends must agree on this order:
			 * an ObjectInputStream constructor blocks waiting for the header that the peer's
			 * ObjectOutputStream constructor emits, so if both sides built their input
			 * stream first the connection would deadlock on handshake.
			 */
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException ex) {
			/*
			 * TODO: report the handshake failure to the caller instead of only printing
			 * it, so run() does not continue with unset streams.
			 */
			ex.printStackTrace();
		}
	}

	private void closeConnection() {

		try {
			if (socket != null && !socket.isClosed()) {
				socket.close();
				logger.info("Socket closed. Thread terminating.");
			}
		} catch (IOException e) {
			logger.error("Failed to close socket: " + e.getMessage());
		}

	}

	/*
	 * Closes this client's socket so the blocked read in run() unblocks and the
	 * thread finishes. Called by Server.stop().
	 */
	public void disconnect() {
		closeConnection();
	}

	/*
	 * The protocol is strictly one Response per Request, in order. Our client
	 * blocks on its own read after every write, so this loop answers every request
	 * exactly once: missing a write would leave that client waiting, and writing
	 * twice would put the stream out of step so later replies arrived against the
	 * wrong request.
	 *
	 * The loop is infinite by design. It ends when the socket closes, which makes
	 * readObject() throw and passes control to the finally block below. That is how
	 * Server.stop() brings handler threads down without needing a shared flag.
	 */
	@Override
	public void run() {
		try {

			this.getStreams();

			while (true) {

				System.out.println("Waiting for input..");
				Request req = (Request) inputStream.readObject();

				RequestType reqtype = req.getType();

				Response resp = new Response();

				/*
				 * Routing by RequestType keeps the protocol open to extension: adding a
				 * feature means adding an enum value and a case here, without touching the
				 * read and write loop around it.
				 *
				 * Login and the two payment operations are the paths we completed first,
				 * because together they exercise the whole stack end to end: a read query, a
				 * write that generates a key, and the authentication that guards both.
				 */
				switch (reqtype) {

				case LOGIN:
					resp = authservice.login(req);
					break;
				case MAKE_PAYMENT:

					resp = paymentService.pay(req);
					break;

				case GET_MY_PAYMENTS:

					resp = paymentService.getPayments(req);
					break;

				default:
					/*
					 * The remaining request types fall through to the empty Response built
					 * above. The appointment, chat, complaint, medical record and vitals
					 * services are written as stubs so far, so their cases are added here as
					 * each one is finished.
					 *
					 * TODO: route the remaining RequestType values to their services as those
					 * services are completed, working through appointments, medical records,
					 * complaints, vitals and chat.
					 */
					break;

				}

				/*
				 * A fresh Response is built on every pass of the loop rather than reusing one
				 * instance. That is deliberate: ObjectOutputStream remembers objects by
				 * identity, so writing the same instance twice would send the client the
				 * first version again even after we changed its contents.
				 *
				 * TODO: call flush() after this write, matching what the client does, so a
				 * Response cannot sit in the buffer while the client waits on its read.
				 */
				outputStream.writeObject(resp);

			}

		} catch (ClassNotFoundException e) {
			/*
			 * The client sent a class this server cannot resolve, which in practice means
			 * the two sides were built against different versions of careplus_common.
			 */
			logger.error("Class not found Exception:" + e.getMessage());

		} catch (IOException e) {
			/*
			 * Normal termination path as well as the error path: this is what a client
			 * disconnecting, or Server.stop() closing the socket, looks like from inside
			 * the blocked read. It is not necessarily a fault.
			 */
			logger.error("Error:" + e.getMessage());
		}

		finally {

			/*
			 * Runs even when the socket is already closed, since closeConnection guards on
			 * isClosed(). Closing twice is harmless and guarantees the descriptor is
			 * released no matter which path ended the loop.
			 */
			closeConnection();
		}

	}
}