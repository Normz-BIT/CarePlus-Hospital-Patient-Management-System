package com.careplus.server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.careplus.server.util.ServerConsole;

/*
 * Server
 * Accepts client connections and hands each one to a ClientHandler thread.
 *
 * The accept loop blocks, so start() runs it on its own thread. Constructing a
 * Server no longer starts it - the caller decides when to listen.
 */
public class Server {

	private ServerSocket serverSock;
	private Thread acceptThread;

	private final List<ClientHandler> handlers = new ArrayList<>();

	private volatile boolean running = false;

	private final int port = 8888;
	private final int backlogCount = 50;

	private ServerConsole console;

	private static final Logger logger = LogManager.getLogger(Server.class);

	public Server() {

	}

	/*
	 * Mirrors server activity into the given console, e.g. the server window.
	 */
	public void setConsole(ServerConsole console) {
		this.console = console;
	}

	public boolean isRunning() {
		return running;
	}

	public int getPort() {
		return port;
	}

	/*
	 * Opens the socket and begins accepting clients on a background thread.
	 * Returns false when the server is already running or the port is taken.
	 */
	public boolean start() {

		if (running) {

			report("Server is already running.");
			return false;
		}

		try {
			serverSock = new ServerSocket(port, backlogCount);

		} catch (IOException e) {

			logger.error("The server socket could not be opened on port {}", port, e);
			report("Unable to listen on port " + port + ": " + e.getMessage());

			return false;
		}

		running = true;

		acceptThread = new Thread(this::waitForRequests, "careplus-accept");
		acceptThread.setDaemon(true);
		acceptThread.start();

		logger.info("Server started on port {}", port);
		report("Server started on port " + port + ".");

		return true;
	}

	/*
	 * Stops accepting clients and closes every open client connection.
	 */
	public boolean stop() {

		if (!running) {

			report("Server is not running.");
			return false;
		}

		running = false;

		// Closing the socket makes the blocked accept() throw, ending the loop.
		closeConnection();

		synchronized (handlers) {

			for (ClientHandler handler : handlers)
				handler.disconnect();

			handlers.clear();
		}

		logger.info("Server stopped");
		report("Server stopped.");

		return true;
	}

	private void closeConnection() {

		if (serverSock != null && !serverSock.isClosed()) {

			try {
				serverSock.close();

			} catch (IOException e) {
				logger.warn("The server socket could not be closed cleanly", e);
			}
		}
	}

	private void waitForRequests() {

		logger.info("Server is listening on port {}", port);

		while (running) {

			try {

				Socket socket = serverSock.accept();

				String clientId = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();

				logger.info("Client connected: {}", clientId);
				report("Client connected: " + clientId);

				ClientHandler clientHandler = new ClientHandler(socket);
				clientHandler.setName(clientId);

				synchronized (handlers) {
					handlers.add(clientHandler);
				}

				clientHandler.start();

			} catch (SocketException e) {

				// Expected when stop() closes the socket out from under accept().
				if (running) {
					logger.error("The server socket failed unexpectedly", e);
					report("Server socket error: " + e.getMessage());
				}

			} catch (IOException e) {

				logger.error("A client could not be accepted", e);
				report("Failed to accept a client: " + e.getMessage());
			}
		}

		logger.info("Server has stopped listening on port {}", port);
	}

	private void report(String message) {

		if (console != null) {
			console.println(message);
		}
	}
}
