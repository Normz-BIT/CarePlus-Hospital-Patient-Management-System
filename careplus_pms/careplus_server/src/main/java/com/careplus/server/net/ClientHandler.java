package com.careplus.server.net;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import com.careplus.common.model.Patient;
import com.careplus.common.net.Request;
import com.careplus.common.net.RequestType;
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

			RequestType reqtype = req.getType();

			Response resp = new Response();

			switch (reqtype) {

			case LOGIN:
				
				resp.setSuccess(true);

				Patient test1 = new Patient("PT1001","Dave","Brown","DBrowan@email.com","12312312","Here I AM",List.of());
				resp.setData(test1);
				
				break;
			default:
				break;

			}

			outputStream.writeObject(resp);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
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