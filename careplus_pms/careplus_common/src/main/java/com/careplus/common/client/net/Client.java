package com.careplus.common.client.net;

import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.common.net.Request;
import com.careplus.common.net.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
/*
 * Handles connection to server
 */
import java.io.ObjectOutputStream;

public class Client {

	private static final Logger logger = LogManager.getLogger(Client.class);
	/*
	 * These are static so the whole client shares one socket to the server. Fine
	 * for a desktop app where one person is signed in at a time, and it means every
	 * controller can just call send() without having to get hold of a connection
	 * first or pass one around.
	 */
	private static Socket socket;
	private static ObjectInputStream inputStream;
	private static ObjectOutputStream outputStream;



	/*
	 * Server address is hardcoded rather than read from a config file, so pointing
	 * the client at anything other than localhost means rebuilding. The port has to
	 * match the one hardcoded in the server's Server class.
	 */
	private final static String host = "localhost";
	private final static int port = 8888;

	public Client() {

		this.createConnection();
		this.getStreams();

	}

	private void createConnection() {

		try {
			socket = new Socket(host, port);
			/*
			 * Keepalive on so the OS spots a server that's gone away, instead of us
			 * sitting on a dead socket forever. This used to call getKeepAlive(), which
			 * only reads the flag and does nothing at all.
			 */
			socket.setKeepAlive(true);
		} catch (IOException ex) {
			logger.error("Could not connect to the server at {}:{}", host, port, ex);
		}
	}

	private void getStreams() {
		try {
			/*
			 * Don't swap these two lines. Building an ObjectInputStream blocks until it
			 * reads a header that the other side only sends when its ObjectOutputStream is
			 * built. Make the input stream first here and we'd deadlock with the server,
			 * which does output before input just like this.
			 */
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException ex) {
			logger.error("Could not open streams to the server", ex);
		}
	}

	/*
	 * The one way every controller talks to the server. Putting all the traffic
	 * behind a single method means the request and response format is written down
	 * in exactly one place, and the controllers just deal in Request and Response
	 * objects without knowing anything about sockets or streams.
	 *
	 * Worth knowing this blocks: it writes the request then sits waiting for the
	 * reply before it returns. Controllers call it straight from Swing button
	 * handlers, so the whole window freezes while the server answers. Fine at our
	 * size, but doing it properly would mean a SwingWorker, and some
	 * synchronization in here once more than one thread can call it.
	 */
	public static Response send(Request request) {

		Response response = new Response();

		/*
		 * Reconnecting in here means no controller has to worry about the connection
		 * itself. They just call send() and it works whether or not the socket
		 * survived since last time.
		 */
		if (!isConnected()) {

			new Client();
		}

		try {

			outputStream.writeObject(request);
			outputStream.flush();

			response = (Response) inputStream.readObject();

		} catch (IOException ioe) {

			/*
			 * The empty Response we made above goes back on every failure path, so a
			 * controller always gets an object and can just check getSuccess() instead of
			 * null checking every single call.
			 */
			logger.error("Request {} failed against the server", request.getType(), ioe);

		} catch (ClassNotFoundException cnfe) {
			/*
			 * The server sent back a class this client doesn't have. Usually means the
			 * two sides were built against different versions of careplus_common, so
			 * rebuild both and try again.
			 */
			logger.error("The server sent a class this client doesn't know", cnfe);

		} catch (Exception e) {
			logger.error("Unexpected error sending request {}", request.getType(), e);
		}

		return response;

	}

	public static Socket getSocket() {
		return socket;
	}

	/*
	 * Only tells us about our end of the socket, which is what send() uses to
	 * decide whether to reconnect before writing.
	 */
	public static boolean isConnected() {

		return socket != null && !socket.isClosed();


	}

	public static void disconnect() {
		try {
			if (socket != null && !socket.isClosed()) {
				/*
				 * Close the streams before the socket so anything still buffered gets flushed
				 * out first. Close the socket first and whatever's pending gets thrown away,
				 * so the server sees a half-written object instead of a clean disconnect.
				 */
				outputStream.close();
				inputStream.close();
				socket.close();
			}
		} catch (IOException e) {
			logger.error("Could not disconnect cleanly from the server", e);
		}
	}
}