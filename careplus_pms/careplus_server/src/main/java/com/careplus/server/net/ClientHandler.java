package com.careplus.server.net;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
import com.careplus.common.net.Response;
import com.careplus.server.service.AuthService;
import com.careplus.server.service.PaymentService;

public class ClientHandler extends Thread {
	private Socket socket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;

	private AuthService authservice;
	private PaymentService paymentService;

	public ClientHandler(Socket socket) {
		this.socket = socket;
		authservice = new AuthService();
		paymentService = new PaymentService();
		
		
	}

	private void getStreams() {
		try {
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void closeConnection() {

		try {
			if (socket != null && !socket.isClosed()) {
				socket.close();
				System.out.println("Socket closed. Thread terminating.");
			}
		} catch (IOException e) {
			System.out.println("Failed to close socket: " + e.getMessage());
		}

	}

	@Override
	public void run() {
		try {

			this.getStreams();

			while (true) {

				System.out.println("Waiting for input..");
				Request req = (Request) inputStream.readObject();

				RequestType reqtype = req.getType();

				Response resp = new Response();

				switch (reqtype) {

				case LOGIN:
					resp = authservice.login(req);
					break;
				case MAKE_PAYMENT:
			
					resp = paymentService.pay(req);
			
				case GET_MY_PAYMENTS:
					
					resp = paymentService.getPayments(req);
					
				default:
					break;

				}

				outputStream.writeObject(resp);

			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Class not found Exception:" + e.getMessage());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error:" + e.getMessage());
		}

		finally {

			closeConnection();
		}

	}
}