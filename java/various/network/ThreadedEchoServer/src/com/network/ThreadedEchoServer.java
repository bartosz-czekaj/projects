package com.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadedEchoServer  implements Runnable {
    private static Socket clientSocket;

    private ThreadedEchoServer(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public static void Establish() {
        System.out.println("Threaded Echo Server");
        try (ServerSocket serverSocket = new ServerSocket(6000)) {
            while (true) {
                System.out.println("Waiting for connection.....");
                clientSocket = serverSocket.accept();
                ThreadedEchoServer tes = new ThreadedEchoServer(clientSocket);
                new Thread(tes).start();
            }
        } catch (IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
        }
        System.out.println("Threaded Echo Server Terminating");
    }

    @Override
    public void run() {
        System.out.println("Connected to client using ["+ Thread.currentThread() + "]");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
             String inputLine;
             while ((inputLine = br.readLine()) != null) {
                System.out.println("Client request ["+ Thread.currentThread() + "]: " + inputLine);
                out.println(inputLine);
            }
            System.out.println("Client [" + Thread.currentThread() + " connection terminated");
        } catch (IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
        }
    }
}
