package com.network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadedTCPServer implements Runnable {

    private static Socket clientSocket;
    private boolean quitCommandLoop = false;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader = null;
    private static final int controlPort = 1021;

    private ThreadedTCPServer(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public static void Establish() {
        System.out.println("FTP Server start");
        try  {

            ServerSocket serverSocketChannel = new ServerSocket(controlPort, 1);

            while (true) {
                System.out.println("Waiting for request ...");
                Socket socketChannel = serverSocketChannel.accept();

                ThreadedTCPServer tes = new ThreadedTCPServer(socketChannel);
                new Thread(tes).start();
            }
        } catch (IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
        }
        System.out.println("FTP Server Terminating");
    }

    @Override
    public void run() {
        System.out.println("Connected to client using ["+ Thread.currentThread() + "]");
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            printWriter.println("220 Livestream FTP Server.");

            CommandHandler cs = new CommandHandler(printWriter);

            bufferedReader.
                    lines().
                    forEach(cs::handle);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
