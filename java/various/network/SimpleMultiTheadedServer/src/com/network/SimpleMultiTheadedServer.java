package com.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.NumberFormat;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleMultiTheadedServer implements Runnable {
    private static ConcurrentHashMap<String, Float> map;
    private Socket clientSocket;


    static {
        map = new ConcurrentHashMap<>();
        map.put("Axle", 238.50f);
        map.put("Gear", 45.55f);
        map.put("Wheel", 86.30f);
        map.put("Rotor", 8.50f);
    }

    SimpleMultiTheadedServer(Socket socket) {
        this.clientSocket = socket;
    }

    public static void Establish() {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            while (true) {
                System.out.println("Listening for a client connection");
                Socket socket = serverSocket.accept();
                System.out.println("Connected to a Client");
                new Thread(new SimpleMultiTheadedServer(socket)).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void run() {
        System.out.println("Client Thread Started");

        try (BufferedReader bis = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintStream out = new PrintStream(clientSocket.getOutputStream())) {

            final String partName = bis.readLine();
            if(map.containsKey(partName)) {
                final float price = map.get(partName);
                out.println(price);
                NumberFormat nf = NumberFormat.getCurrencyInstance();
                System.out.println("Request for " + partName + " and returned a price of " + nf.format(price));
            } else {
                out.println("Part " + partName + " is unavailable");
                System.out.println("Request for " + partName + " cannot be processed");
            }


            clientSocket.close();
            System.out.println("Client Connection Terminated");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println("Client Thread Terminated");
    }
}
