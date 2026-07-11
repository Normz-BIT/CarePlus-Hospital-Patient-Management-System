package com.careplus.server.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server {

	ServerSocket serverSock;
	Socket socket;
	ObjectOutputStream outputStream;
	ObjectInputStream inputStream;
	int port = 8888;
	int backlogCount = 1;

	private static final Logger logger = LogManager.getLogger(Server.class);
	
	public Server() {
		this.createConnection();
		this.waitForRequests();

	}

	public void createConnection() {
		try {
			serverSock = new ServerSocket(port, backlogCount);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void waitForRequests() {

		ClientHandler clientHandler = null;

		try {
			logger.info("Server is listening on port " + port);
			while (true) {
				
				socket = serverSock.accept();

				// create a new thread for each client
				clientHandler = new ClientHandler(socket);
				clientHandler.getStreams();
				clientHandler.start();

			}
		} catch (EOFException ex) {
			logger.warn("Client has terminted connections with the server" + ex.getMessage());
		} catch (IOException ex) {
			ex.printStackTrace();
		} 
	}
}
