package com.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class UDPEchoClient {
    public static void Establish() {
        System.out.println("UDP Echo Client Started");
        try {
            SocketAddress remote = new InetSocketAddress("127.0.0.1", 9000);
            DatagramChannel channel = DatagramChannel.open();
            channel.connect(remote);

            String message = "The message";
            ByteBuffer buffer = ByteBuffer.allocate(message.length());
            buffer.put(message.getBytes());

            buffer.flip();

            channel.write(buffer);
            System.out.println("Sent: [" + message + "]");

            buffer.clear();
            channel.read(buffer);
            buffer.flip();
            System.out.print("Received: [");
            while(buffer.hasRemaining()) {
                System.out.print((char)buffer.get());
            }
            System.out.println("]");
        }
        catch (IOException ex) {
            // Handle exceptions
        }
        System.out.println("UDP Echo Client Terminated");
    }
}
