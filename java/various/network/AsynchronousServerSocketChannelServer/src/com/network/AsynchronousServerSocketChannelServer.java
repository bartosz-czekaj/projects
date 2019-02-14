package com.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.nio.ByteBuffer.allocate;

public class AsynchronousServerSocketChannelServer {
    public AsynchronousServerSocketChannelServer() {
        System.out.println("Asynchronous Server Started");
        try (AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open()) {

            InetSocketAddress hostAddress = new InetSocketAddress("localhost", 5000);
            serverChannel.bind(hostAddress);
            System.out.println("Waiting for client to connect... ");
            Future acceptResult = serverChannel.accept();

            try (AsynchronousSocketChannel clientChannel = (AsynchronousSocketChannel) acceptResult.get()) {
                System.out.println("Messages from client: ");
                while ((clientChannel != null) && (clientChannel.isOpen())) {
                    ByteBuffer buffer = allocate(32);
                    Future result = clientChannel.read(buffer);
                    // Wait until buffer is ready using
                    //result.get();
                    //result.get(10, TimeUnit.SECONDS);

                    while (!result.isDone()) {
                        // do nothing
                    }
                    // one of three techniques to be discussed
                    buffer.flip();
                    String message = new String(buffer.array()).trim();
                    System.out.println("Message: " + message);
                    if (message.equalsIgnoreCase("quit")) {
                        break;
                    }



                }

            } /*catch (TimeoutException e) {
                e.printStackTrace();
            }*/

        } catch (IOException | InterruptedException| ExecutionException ex) {
            ex.printStackTrace();
        }

    }
}
