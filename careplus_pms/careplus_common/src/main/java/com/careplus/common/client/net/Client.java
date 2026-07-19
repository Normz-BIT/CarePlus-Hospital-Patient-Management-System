package com.careplus.common.client.net;

import java.net.Socket;

import com.careplus.common.net.Request;
import com.careplus.common.net.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
/*
 * Handles connection to server
 */
import java.io.ObjectOutputStream;

public class Client {
	/*
	 * All connection state is static, so the whole client process shares exactly one
	 * socket to the server. That is intentional for a desktop app with a single
	 * logged in user, but it makes send() below a process wide critical section that
	 * is not actually guarded. See the threading note on send().
	 */
	private static Socket socket;
	private static ObjectInputStream inputStream;
	private static ObjectOutputStream outputStream;



	/*
	 * Server location is compiled in rather than read from config, so pointing the
	 * client at a non local server currently requires a rebuild. The port must stay
	 * in step with the value hardcoded in the server's Server class.
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
			 * getKeepAlive is a read only accessor, so this line queries the flag and
			 * discards the result rather than enabling anything. Enabling TCP keepalive
			 * would require setKeepAlive(true). Left as is because changing it alters
			 * runtime behaviour, but it should not be read as timeout protection.
			 */
			socket.getKeepAlive();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void getStreams() {
		try {
			/*
			 * Order matters and must not be swapped. Constructing an ObjectInputStream
			 * blocks until it has read the stream header its peer writes, and that header
			 * is only flushed when the ObjectOutputStream is constructed. Creating the
			 * input stream first here would deadlock against the server, which builds its
			 * streams in the same output before input order.
			 */
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * The single entry point every controller uses to reach the server.
	 *
	 * Two constraints callers need to know about:
	 *
	 * Blocking: this performs a full write then read round trip and does not return
	 * until the server answers. Every controller in this project calls it directly
	 * from a Swing ActionListener, which means it runs on the Event Dispatch Thread
	 * and freezes the entire UI for the duration. A hung or slow server hangs the
	 * window rather than just the one screen. Moving these calls onto a SwingWorker
	 * would confine the stall to the affected view.
	 *
	 */
	public static Response send(Request request) {

		Response response = new Response();

		/*
		 * Reconnects transparently if the socket was dropped, so callers never handle
		 * connection lifecycle. Note this only detects a locally closed socket: a
		 * server that died without a clean FIN still looks connected until the write
		 * below fails.
		 */
		if (!isConnected()) {

			new Client();
		}

		try {

			outputStream.writeObject(request);
			outputStream.flush();

			response = (Response) inputStream.readObject();

			// System.out.println("Server said: " + in.readObject());
		} catch (IOException ioe) {

			/*
			 * Every failure path below leaves response as null and reports nothing. A
			 * dropped connection is therefore indistinguishable to the caller from a
			 * request the server does not implement, since both yield null. Callers must
			 * null check the return value. Wiring these three blocks to log4j2 is the
			 * outstanding work.
			 */
			// TODO Add log4j2
		} catch (ClassNotFoundException cnfe) {
			/*
			 * Signals that the server sent a class this client's classpath does not have,
			 * which in practice means the two sides were built against different versions
			 * of careplus_common.
			 */
			// TODO Add log4j2

		} catch (Exception e) {
			// TODO Add log4j2
		}

		return response;

	}

	public static Socket getSocket() {
		return socket;
	}

	/*
	 * Reports only on the local end of the socket. isClosed() is false for a
	 * connection whose peer has already vanished, so a true result here is not a
	 * guarantee that the next write will succeed.
	 */
	public static boolean isConnected() {

		return socket != null && !socket.isClosed();


	}

	public static void disconnect() {
		try {
			if (socket != null && !socket.isClosed()) {
				/*
				 * Streams are closed before the socket so buffered bytes are flushed to the
				 * server first. Closing the socket first would discard anything still pending
				 * and give the server a truncated object rather than a clean end of stream.
				 */
				outputStream.close();
				inputStream.close();
				socket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}