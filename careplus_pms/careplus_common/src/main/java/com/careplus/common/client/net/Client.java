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
	// client socket
	private static Socket socket;
	private static ObjectInputStream inputStream;
	private static ObjectOutputStream outputStream;

	private static Response response;

	// right now we are only running locally so set static
	private final static String host = "localhost";
	private final static int port = 8888;

	public Client() {

		this.createConnection();
		this.getStreams();

	}

	private void createConnection() {

		try {
			socket = new Socket(host, port);
			// to prevent timeout
			socket.getKeepAlive();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void getStreams() {
		try {
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static Response send(Request request) {

		response = null;
		
		if (!isConnected()) {
			
			new Client();
		}

		try {

			outputStream.writeObject(request);
			outputStream.flush();

			response = (Response) inputStream.readObject();

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

	public static Socket getSocket() {
		return socket;
	}

	public static boolean isConnected() {
		return socket != null && !socket.isClosed();
	}

	public static void disconnect() {
		try {
			if (socket != null && !socket.isClosed()) {
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