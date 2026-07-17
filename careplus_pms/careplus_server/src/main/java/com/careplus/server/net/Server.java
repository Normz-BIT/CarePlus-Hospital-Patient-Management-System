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
	ClientHandler clientHandler = null;
	int port = 8888;
	int backlogCount = 1;
	String clientId = "";

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

	public void closeConnection() {
		if (serverSock != null && !serverSock.isClosed()) {
			try {
				serverSock.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void waitForRequests() {

		while (true) {
			try {
				logger.info("Server is listening on port " + port);

				socket = serverSock.accept();
				clientId = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
				logger.info("Client connected: " + clientId);

				// create a new thread for each client
				clientHandler = new ClientHandler(socket);
				clientHandler.setName(clientId);
				clientHandler.start();

				logger.info("Active threads: " + Thread.activeCount());

			} catch (EOFException ex) {
				logger.warn("Client has terminted connections with the server" + ex.getMessage());
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
	}
}
