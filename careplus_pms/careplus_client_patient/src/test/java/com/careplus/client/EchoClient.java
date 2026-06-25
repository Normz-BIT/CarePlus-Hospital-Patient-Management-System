package com.careplus.client;

import java.io.*;
import java.net.*;

public class EchoClient {
	public static int test() {
		try {
			Socket socket = new Socket("localhost", 5000);
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			out.writeObject("Client Message");
			out.flush();
			System.out.println("Server said: " + in.readObject());
			socket.close();
			return 1;
		} catch (Exception e) {

		}

		return 0;

	}
}