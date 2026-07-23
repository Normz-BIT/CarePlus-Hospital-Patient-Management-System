package com.careplus.server.controller;


//import com.careplus.server.net.Server;
import com.careplus.server.util.HibernateUtil;
import com.careplus.server.view.ServerView;

public class ServerController {

	public static void main(String[] args) {

		new HibernateUtil();

		new ServerView();
		//new Server();

	}

}
