package com.network;

import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SSLClientSocket {

    public static void Established() throws Exception {
        System.out.println("SSLClientSocket Started");
        SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();

        try (Socket socket = sf.createSocket("localhost", 8000);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    System.out.print("Enter text: ");
                    String inputLine = scanner.nextLine();
                    if ("quit".equalsIgnoreCase(inputLine)) {
                        break;
                    }
                    out.println(inputLine);
                    System.out.println("Server response: " + br.readLine());
                }
            System.out.println("SSLServerSocket Terminated");
        }
    }
}
