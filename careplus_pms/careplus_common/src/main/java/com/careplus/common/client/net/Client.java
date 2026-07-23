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
<<<<<<< HEAD
<<<<<<< HEAD
	// client socket
	private Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
=======
=======
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git
	/*
	 * The connection state is static so the whole client process shares one socket
	 * to the server. That suits a desktop application where a single user is signed
	 * in at a time, and it means controllers can call send() without each one
	 * having to obtain or pass around a connection.
	 */
	private static Socket socket;
	private static ObjectInputStream inputStream;
	private static ObjectOutputStream outputStream;
>>>>>>> stash

<<<<<<< HEAD
<<<<<<< HEAD
	private Response response;
=======
>>>>>>> stash
=======
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git

<<<<<<< HEAD
<<<<<<< HEAD
	// right now we only running locally so set static
=======

	/*
	 * Server location is compiled in rather than read from config, so pointing the
	 * client at a non local server currently requires a rebuild. The port must stay
	 * in step with the value hardcoded in the server's Server class.
	 */
>>>>>>> stash
=======

	/*
	 * Server location is compiled in rather than read from config, so pointing the
	 * client at a non local server currently requires a rebuild. The port must stay
	 * in step with the value hardcoded in the server's Server class.
	 */
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git
	private final static String host = "localhost";
	private final static int port = 8888;

	
	public Client() {

		this.createConnection();
		this.getStreams();

	}
	
	
	public void createConnection()  {

		try {
			socket = new Socket(host, port);
			/*
			 * TODO: this reads the keepalive flag rather than setting it. Change to
			 * setKeepAlive(true) if we want the OS to detect a server that has gone away.
			 */
			socket.getKeepAlive();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	
	
	public void getStreams() {
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

<<<<<<< HEAD
<<<<<<< HEAD
	public Response send(Request request) {
=======
=======
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git
	/*
	 * The single entry point every controller uses to reach the server. Keeping all
	 * traffic behind one method means the request and response format is defined in
	 * exactly one place, and controllers deal in Request and Response objects
	 * without knowing anything about sockets or streams.
	 *
	 * The call is blocking: it writes a request and waits for the matching reply
	 * before returning. Controllers call it from Swing action listeners, so the
	 * interface waits while the server answers.
	 *
	 * TODO: move these calls onto a SwingWorker so the interface stays responsive
	 * while a request is in flight, and add synchronization here once more than one
	 * thread can call send().
	 */
	public static Response send(Request request) {
>>>>>>> stash

<<<<<<< HEAD
<<<<<<< HEAD
		response = null;
=======
=======
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git
		Response response = new Response();

		/*
		 * Reconnecting here means controllers never have to manage the connection
		 * lifecycle themselves; they just call send() and it works whether or not the
		 * socket survived since the last request.
		 */
		if (!isConnected()) {

			new Client();
		}
>>>>>>> stash

		
		// TODO throw all exception to controller 
		// this makes it easier for the program flow and for logging
		try {

			outputStream.writeObject(request);
			outputStream.flush();

			response = (Response) inputStream.readObject();

			// System.out.println("Server said: " + in.readObject());
		} catch (IOException ioe) {

			/*
			 * The empty Response created above is returned on every failure path, so a
			 * controller always gets an object back and checks isSuccess() rather than
			 * having to guard against null on each call.
			 */
			// TODO Add log4j2
		} catch (ClassNotFoundException cnfe) {
			/*
			 * Means the server sent a class this client does not have on its classpath,
			 * which in practice happens when the two sides were built against different
			 * versions of careplus_common.
			 */
			// TODO Add log4j2

		} catch (Exception e) {
			// TODO Add log4j2
		}

		return response;
	}

	public Socket getSocket() {
		return socket;
	}

<<<<<<< HEAD
<<<<<<< HEAD
	public boolean isConnected() {
		return socket != null  && !socket.isClosed();
=======
=======
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git
	/*
	 * Reports on the local end of the socket, which is what send() uses to decide
	 * whether it needs to reconnect before writing.
	 */
	public static boolean isConnected() {

		return socket != null && !socket.isClosed();


<<<<<<< HEAD
>>>>>>> stash
=======
>>>>>>> branch 'development' of https://github.com/Normz-BIT/CarePlus-Hospital-Patient-Management-System.git
	}

	public void disconnect(){
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