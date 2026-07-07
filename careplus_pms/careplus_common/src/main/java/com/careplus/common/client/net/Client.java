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
	private Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;

	private Response response;

	// right now we only running locally so set static
	private final static String host = "localhost";
	private final static int port = 8888;

	
	public Client() {

		this.createConnection();
		this.getStreams();

	}
	
	
	public void createConnection()  {

		try {
			socket = new Socket(host, port);
			// to prevent timeout
			socket.getKeepAlive();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	
	
	public void getStreams() {
		try {
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public Response send(Request request) {

		response = null;

		
		// TODO throw all exception to controller 
		// this makes it easier for the program flow and for logging
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

	public Socket getSocket() {
		return socket;
	}

	public boolean isConnected() {
		return socket != null  && !socket.isClosed();
	}

	public void disconnect(){
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