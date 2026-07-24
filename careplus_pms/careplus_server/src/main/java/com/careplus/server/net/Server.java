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
 * Takes client connections and gives each one to its own ClientHandler thread.
 *
 * The accept loop blocks, so start() runs it on a separate thread. Building a
 * Server doesn't start it listening, the caller decides when with start().
 */
public class Server {

	private ServerSocket serverSock;
	private Thread acceptThread;

	/*
	 * The accept thread adds to this on every new connection and whoever calls
	 * stop() reads it, so every use below is inside synchronized (handlers)
	 */
	private final List<ClientHandler> handlers = new ArrayList<>();

	/*
	 * volatile because stop() writes it from one thread and the accept loop reads
	 * it from another. Without it the accept thread could hang onto a stale true
	 * and keep looping after we've shut down. It also tells the SocketException
	 * handler whether the failure was us closing the socket or something real.
	 */
	private volatile boolean running = false;

	/*
	 * Has to match the port hardcoded in the client's Client class. The backlog is
	 * how many connections the OS will queue up while accept() is busy; past that
	 * clients get refused rather than queued.
	 */
	private final int port = 8888;
	private final int backlogCount = 10;

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
	 * Opens the socket and begins accepting clients on a background thread. Returns
	 * false when the server is already running or the port is taken.
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

		/*
		 * Set this before starting the thread, or the loop could read false straight
		 * away and exit before it does anything.
		 */
		running = true;

		acceptThread = new Thread(() -> waitForRequests(), "careplus-accept");
	
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

		/*
		 * Clear this first so that if the accept thread is mid-loop it sees we're
		 * shutting down and treats the SocketException below as expected instead of
		 * logging it like something went wrong.
		 */
		running = false;

		// Closing the socket makes the blocked accept() throw, ending the loop.
		closeConnection();

		/*
		 * Disconnecting a handler closes its socket, which is what wakes up that
		 * thread's blocked readObject() and lets it run its finally block. We don't
		 * join() them, so stop() returns before the handler threads have actually
		 * finished. They die off on their own a moment later.
		 */
		synchronized (handlers) {

			for (ClientHandler handler : handlers) {
				handler.disconnect();
			}

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

	/*
	 * The accept loop. One thread per connected client, no pool and no upper limit.
	 * That covers the multi-client requirement from the brief and means a slow
	 * database call only holds up the client that asked for it. The catch is that
	 * ten clients means ten threads, so this would want an ExecutorService before
	 * it could handle a whole hospital.
	 */
	private void waitForRequests() {

		logger.info("Server is listening on port {}", port);

		while (running) {

			try {

				Socket socket = serverSock.accept();

				String clientId = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();

				logger.info("Client connected: {}", clientId);
				report("Client connected: " + clientId);

				ClientHandler clientHandler = new ClientHandler(socket);
				/*
				 * Naming the thread after the client's address and port means we can trace a
				 * log line or a thread dump back to a specific connection.
				 */
				clientHandler.setName(clientId);

				/*
				 * Registered before being started, so a stop() that lands between these two
				 * statements still finds the handler and closes its socket. Starting first
				 * would leave a window where a live connection is invisible to shutdown and
				 * would outlive the server.
				 */
				synchronized (handlers) {
					handlers.add(clientHandler);
				}

				clientHandler.start();

			} catch (SocketException e) {

				/*
				 * Expected when stop() closes the socket out from under accept(). The running
				 * check is what distinguishes our own orderly shutdown from a genuine socket
				 * fault, so a deliberate stop does not spam the log with errors.
				 */
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
			console.showln(message);
		}
	}
}
