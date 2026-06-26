package com.careplus.common.client.net;

import java.net.Socket;
import java.net.SocketException;

import com.careplus.common.net.Request;
import com.careplus.common.net.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
/*
 * Handles connection to server
 */
import java.io.ObjectOutputStream;

public class ServerConnection {
	// client socket
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	private Response response;

	// right now we only running locally so fix these
	private final static String host = "localhost";
	private final static int port = 5000;

	public void connect() throws IOException, SocketException {

		this.socket = new Socket(host, port);
		// prevent timeout
		socket.getKeepAlive();

		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());

	}

	// TODO add session key to request
	public Response send(Request request) {

		response = null;

		
		// TODO throw all exception to controller 
		// this makes it easier for the program flow and for logging
		try {

			out.writeObject(request);
			out.flush();

			response = (Response) in.readObject();

			// System.out.println("Server said: " + in.readObject());
		} catch (IOException ioe) {

			// TODO Add log4j2
		} catch (ClassNotFoundException cnfe) {
			// TODO Add log4j2

		} catch (Exception e) {
			// TODO Add log4j2
		}

		return response;
	}

	public Socket getSocket() {
		return socket;
	}

	public boolean isConnected() {
		return socket != null && socket.isConnected() && !socket.isClosed();
	}

	public void disconnect() throws IOException {
		if (socket != null && !socket.isClosed()) {
			socket.close();
		}
	}
}