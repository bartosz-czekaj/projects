package com.network;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class UDPEchoServer {
    public static void Establish() {
        int port = 9000;
        System.out.println("UDP Echo Server Started");
        try (DatagramChannel channel = DatagramChannel.open();
             DatagramSocket socket = channel.socket();) {

            SocketAddress address = new InetSocketAddress(port);
            socket.bind(address);

            ByteBuffer buffer = ByteBuffer.allocateDirect(65507);

            while (true) {
                // Get message
                SocketAddress client = channel.receive(buffer);
                buffer.flip();
                // Display message
                buffer.mark();
                System.out.print("Received: [");
                StringBuilder message = new StringBuilder();
                while (buffer.hasRemaining()) {
                    message.append((char) buffer.get());
                }
                System.out.println(message + "]");
                buffer.reset();
                // Return message
                channel.send(buffer, client);
                System.out.println("Sent: [" + message + "]");
                buffer.clear();
            }
        }
        catch (IOException ex) {
        // Handle exceptions
        }
        System.out.println("UDP Echo Server Terminated");
    }
}
