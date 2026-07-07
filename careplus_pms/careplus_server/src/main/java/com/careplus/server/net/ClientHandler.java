package com.careplus.server.net;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.careplus.common.model.Complaint;
import com.careplus.common.net.Request;
import com.careplus.common.net.Response;

public class ClientHandler extends Thread {
	private Socket socket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	public void getStreams() {
		try {
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {

			Request req = (Request) inputStream.readObject();

			Complaint complaint = (Complaint) req.getParams().get("complaint");
			System.out.println("Server: " + req.getType() + " " + complaint.getDescription());

			complaint.setDescription("Modified by server");
			
			Response resp = new Response(true, "Completed", (Object) complaint);
			
			outputStream.writeObject(resp);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				if (socket != null && !socket.isClosed()) {
					socket.close();
					System.out.println("Socket closed. Thread terminating.");
				}
			} catch (IOException e) {
				System.out.println("Failed to close socket: " + e.getMessage());
			}
		}
	}
}