package com.careplus.server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

<<<<<<< HEAD
=======

import com.careplus.server.view.ServerView;


import com.careplus.server.util.ServerConsole;

/*
 * Server
 * Accepts client connections and hands each one to a ClientHandler thread.
 *
 * The accept loop blocks, so start() runs it on its own thread. Constructing a
 * Server no longer starts it - the caller decides when to listen.
 */

>>>>>>> stash
public class Server {


	ServerSocket serverSock;
	Socket socket;
	ObjectOutputStream outputStream;
	ObjectInputStream inputStream;
	int port = 8888;
	int backlogCount = 1;
<<<<<<< HEAD
=======
	String clientId = "";
	private boolean running = true;
	private ServerView view;
	

	private ServerSocket serverSock;
	private Thread acceptThread;

	/*
	 * Touched by the accept thread on every new connection and by whichever thread
	 * calls stop(), so every access below is wrapped in synchronized (handlers). A
	 * plain ArrayList is deliberate rather than a concurrent collection: stop()
	 * needs to iterate the whole list and clear it as one atomic unit, which a
	 * CopyOnWriteArrayList would not give without an outer lock anyway. Both
	 * critical sections are short enough that lock contention is not a concern.
	 */
	private final List<ClientHandler> handlers = new ArrayList<>();

	/*
	 * volatile because it is written by the thread calling stop() and read by the
	 * accept thread's loop condition. Without it the accept thread could cache a
	 * stale true and keep looping after shutdown. It also tells the SocketException
	 * handler whether a failure was our own doing or a genuine fault.
	 */
	private volatile boolean running = false;

	/*
	 * Must match the port compiled into the client's Client class. The backlog caps
	 * how many connections the OS queues while accept() is busy; beyond this,
	 * further clients are refused rather than queued.
	 */
	private final int port = 8888;
	private final int backlogCount = 50;

	private ServerConsole console;

>>>>>>> stash

	private static final Logger logger = LogManager.getLogger(Server.class);

	
<<<<<<< HEAD
	public Server() {
		this.createConnection();
		this.waitForRequests();

	}
=======
	public Server(ServerView view) {
	    this.view = view;
	    createConnection();
	    waitForRequests();

	public Server() {
	
	//public Server() {
		//this.createConnection();
		//this.waitForRequests();
>>>>>>> stash

<<<<<<< HEAD
=======

	//}
	public void logMessage(String msg) {

	    logger.info(msg);

	    if (view != null) {
	        view.appendMessage(msg);
	    }
	}
>>>>>>> stash
	public void createConnection() {
=======
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

		/*
		 * Set before the thread starts so the loop condition cannot observe false and
		 * exit immediately.
		 */
		running = true;

		acceptThread = new Thread(this::waitForRequests, "careplus-accept");
		/*
		 * Daemon so that closing the server window terminates the JVM even if stop()
		 * was never called. A non daemon accept thread parked in accept() would keep
		 * the process alive indefinitely with no visible window.
		 */
		acceptThread.setDaemon(true);
		acceptThread.start();

		logger.info("Server started on port {}", port);
		report("Server started on port " + port + ".");

		return true;
	}
<<<<<<< HEAD
=======

	public void closeConnection() {
		 running = false;


	/*
	 * Stops accepting clients and closes every open client connection.
	 */
	public boolean stop() {

		if (!running) {

			report("Server is not running.");
			return false;
		}

		/*
		 * Cleared first so that the accept thread, if it happens to be mid loop, sees
		 * the shutdown and treats the SocketException below as expected rather than
		 * logging it as a fault.
		 */
		running = false;

		// Closing the socket makes the blocked accept() throw, ending the loop.
		closeConnection();

		/*
		 * Disconnecting each handler closes its socket, which is what unblocks that
		 * thread's readObject() and lets it run its finally block. There is no join()
		 * here, so stop() returns before the handler threads have necessarily died;
		 * they terminate on their own shortly after.
		 */
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

				e.printStackTrace();

				logger.warn("The server socket could not be closed cleanly", e);

			}
		}
	}
	public void stopServer() {
	    running = false;
	    closeConnection();
	}
>>>>>>> stash


	public void waitForRequests() {

<<<<<<< HEAD
		ClientHandler clientHandler = null;
=======
	/*
	 * The accept loop. Runs one thread per connected client with no upper bound and
	 * no thread pool, which satisfies the multi client requirement and keeps a slow
	 * database call from blocking anyone but the client that made it. The tradeoff
	 * is that N clients cost N threads, so this design would need an ExecutorService
	 * before it could handle a large ward.
	 */
	private void waitForRequests() {

		while (running) {

		logger.info("Server is listening on port {}", port);

		while (running) {


			try {
>>>>>>> stash

<<<<<<< HEAD
		try {
			logger.info("Server is listening on port " + port);
			while (true) {
				
=======

>>>>>>> stash
				socket = serverSock.accept();

				Socket socket = serverSock.accept();

				// create a new thread for each client
<<<<<<< HEAD
				clientHandler = new ClientHandler(socket);
				clientHandler.getStreams();
=======
				clientHandler = new ClientHandler(socket, this);

				String clientId = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();

				logger.info("Client connected: {}", clientId);
				report("Client connected: " + clientId);

				ClientHandler clientHandler = new ClientHandler(socket);
				/*
				 * Naming the thread after the client's address and port makes thread dumps and
				 * log output traceable back to a specific connection.
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

>>>>>>> stash
				clientHandler.start();

<<<<<<< HEAD
			}
		} catch (EOFException ex) {
			logger.warn("Client has terminted connections with the server" + ex.getMessage());
		} catch (IOException ex) {
			ex.printStackTrace();
		} 
=======

				logMessage("Active threads: " + Thread.activeCount());
			} catch (SocketException e) {

			} catch (EOFException ex) {
				logger.warn("Client has terminted connections with the server" + ex.getMessage());
			} catch (IOException ex) {
				//ex.printStackTrace();
				 if (running) {
		                ex.printStackTrace();
		            } else {
		                logger.info("Server stopped.");
		            }
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
			console.println(message);
		}
>>>>>>> stash
	}
}
