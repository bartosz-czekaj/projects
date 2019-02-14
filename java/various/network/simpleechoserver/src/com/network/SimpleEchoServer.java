package com.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleEchoServer {

    @SuppressWarnings("unused")
    public static void Establish() {
        try {
            final ServerSocket socket = new ServerSocket(6000);
            while (true) {
                System.out.println("Waiting... ");
                final Socket clientSocket = socket.accept();
                System.out.println("Established... ");
                final BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                final PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

                String line = reader.readLine();

                System.out.println("Readline: "+ line);

                writer.println(line);
                writer.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
