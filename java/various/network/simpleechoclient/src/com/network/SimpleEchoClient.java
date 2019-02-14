package com.network;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
//import java.util.Scanner;

public class SimpleEchoClient {

    public static void Establish() {
        try {
            InetAddress localAddress = InetAddress.getLocalHost();
            Scanner scanner = new Scanner(System.in);

            while (true) {
                final Socket socket = new Socket(localAddress, 6000);

                final BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                final PrintWriter writer = new PrintWriter(
                        socket.getOutputStream(), true);

                System.out.print("Enter text: ");
                String inputLine = scanner.nextLine();
                if ("quit".equalsIgnoreCase(inputLine)) {
                    break;
                }
                writer.println(inputLine);
                String response = reader.readLine();
                System.out.println("Server response: " + response);

                writer.close();
            }
        } catch (Exception ex) {
            System.out.println("Error: "+ex.getMessage());
        }

    }
}
