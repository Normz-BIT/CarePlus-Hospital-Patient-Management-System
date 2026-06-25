package com.careplus.server;

import java.io.*;
import java.net.*;

public class EchoServerTest {

	public static int test() {

		ServerSocket server = null;

		try {
	
			server = new ServerSocket(5000);
			
			System.out.println("Echo server listening on 5000...");
			
			Socket socket = server.accept();
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			
			String msg = (String) in.readObject();
			
			System.out.println("Received: " + msg);
			
			out.writeObject("Echo: " + msg);
			
			out.flush();
			server.close();
			return 1;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}
}