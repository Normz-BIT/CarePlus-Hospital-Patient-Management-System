package com.careplus.server.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.server.view.ServerView;

public class Server {

	ServerSocket serverSock;
	Socket socket;
	ObjectOutputStream outputStream;
	ObjectInputStream inputStream;
	ClientHandler clientHandler = null;
	int port = 8888;
	int backlogCount = 1;
	String clientId = "";
	private boolean running = true;
	private ServerView view;
	
	private static final Logger logger = LogManager.getLogger(Server.class);
	
	public Server(ServerView view) {
	    this.view = view;
	    createConnection();
	    waitForRequests();
	}
	
	//public Server() {
		//this.createConnection();
		//this.waitForRequests();

	//}
	public void logMessage(String msg) {

	    logger.info(msg);

	    if (view != null) {
	        view.appendMessage(msg);
	    }
	}
	public void createConnection() {
		try {
			serverSock = new ServerSocket(port, backlogCount);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	public void closeConnection() {
		 running = false;
		if (serverSock != null && !serverSock.isClosed()) {
			try {
				serverSock.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void stopServer() {
	    running = false;
	    closeConnection();
	}

	public void waitForRequests() {
		logMessage("Server is listening on port " + port);

		while (running) {
			try {

				socket = serverSock.accept();
				clientId = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
				logMessage("Client connected: " + clientId);

				// create a new thread for each client
				clientHandler = new ClientHandler(socket, this);
				clientHandler.setName(clientId);
				clientHandler.start();

				logMessage("Active threads: " + Thread.activeCount());

			} catch (EOFException ex) {
				logger.warn("Client has terminted connections with the server" + ex.getMessage());
			} catch (IOException ex) {
				//ex.printStackTrace();
				 if (running) {
		                ex.printStackTrace();
		            } else {
		                logger.info("Server stopped.");
		            }
			}

		}
	}

}
