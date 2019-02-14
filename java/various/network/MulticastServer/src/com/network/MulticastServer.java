package com.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;

public class MulticastServer {

    private static DatagramSocket serverSocket = null;

    public static void Establish() {
        System.out.println("Multicast  Time Server");

        try {
            serverSocket = new DatagramSocket();
            sender();
        } catch (SocketException ex) {
            System.out.println("SocketException: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
        }
    }

    private static void sender() throws IOException {
        while (true) {
            String dateText = new Date().toString();
            byte[] buffer = new byte[256];
            buffer = dateText.getBytes();

            InetAddress group = InetAddress.getByName("224.0.0.0");
            DatagramPacket packet;
            packet = new DatagramPacket(buffer, buffer.length,
                    group, 8888);
            serverSocket.send(packet);
            System.out.println("Time sent: " + dateText);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                System.out.println("InterruptedException: " + ex.getMessage());
            }
        }
    }
}
