package com.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleWebServer {
    public SimpleWebServer() {
        System.out.println("Webserver Started");
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            while (true) {
                System.out.println("Waiting for client request");
                Socket remote = serverSocket.accept();
                System.out.println("Connection made");
                new Thread(new ClientHandler(remote)).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
