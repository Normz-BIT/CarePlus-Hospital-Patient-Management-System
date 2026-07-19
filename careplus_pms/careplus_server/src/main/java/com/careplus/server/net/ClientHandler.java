package com.careplus.server.net;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
			 * Swallowing the failure here leaves both streams null and lets run() proceed
			 * to dereference them, turning a clear IOException into a
			 * NullPointerException one line later. Failing the thread outright would be
			 * more honest.
			 */
			ex.printStackTrace();
		}
	}

	private void closeConnection() {

		try {
			if (socket != null && !socket.isClosed()) {
				socket.close();
				System.out.println("Socket closed. Thread terminating.");
			}
		} catch (IOException e) {
			System.out.println("Failed to close socket: " + e.getMessage());
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
	 * Strictly one Response per Request, in order. The client blocks on its own read
	 * after every write, so this loop must answer every request exactly once:
	 * skipping a write would hang that client forever, and writing twice would
	 * desynchronise the stream so every later reply arrives against the wrong
	 * request.
	 *
	 * The loop is deliberately infinite. It ends only when the socket closes, which
	 * makes readObject() throw and drops control into the finally block. That is the
	 * mechanism Server.stop() relies on to terminate handler threads.
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
				 * Only three of the 26 RequestType values are wired up. Everything else falls
				 * to the default branch below and silently returns the empty Response created
				 * above, because the Appointment, Chat, Complaint and MedicalRecord services
				 * are still stubs. To the client this is indistinguishable from a network
				 * failure, since both surface as a null or empty result.
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
					break;

				}

				/*
				 * Two serialization footguns apply to this write.
				 *
				 * There is no flush() here, unlike the client side, so a Response can sit in
				 * the ObjectOutputStream buffer instead of reaching the client that is already
				 * blocked reading it. Any apparent hang on a request should be investigated
				 * here first.
				 *
				 * There is also no reset(). ObjectOutputStream caches objects by identity, so
				 * if a service ever returns the same instance twice with mutated contents, the
				 * client receives the original stale copy rather than the update. Building a
				 * fresh Response per iteration, as this loop does, is what currently keeps
				 * that from biting.
				 */
				outputStream.writeObject(resp);

			}

		} catch (ClassNotFoundException e) {
			/*
			 * The client sent a class this server cannot resolve, which in practice means
			 * the two sides were built against different versions of careplus_common.
			 */
			System.out.println("Class not found Exception:" + e.getMessage());

		} catch (IOException e) {
			/*
			 * Normal termination path as well as the error path: this is what a client
			 * disconnecting, or Server.stop() closing the socket, looks like from inside
			 * the blocked read. It is not necessarily a fault.
			 */
			System.out.println("Error:" + e.getMessage());
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