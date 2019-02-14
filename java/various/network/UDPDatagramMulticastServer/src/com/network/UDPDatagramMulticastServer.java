package com.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.time.Instant;

public class UDPDatagramMulticastServer {
    public static void Establish(){
        try {

            System.setProperty("java.net.preferIPv6Stack", "true");
            DatagramChannel channel = DatagramChannel.open();
            NetworkInterface networkInterface = NetworkInterface.getByName("wlan0");
            channel.setOption(StandardSocketOptions.IP_MULTICAST_IF, networkInterface);
            InetSocketAddress group = new InetSocketAddress("FF01:0:0:0:0:0:0:FC",9003);

            String message = "The message at: " + Instant.now().toString()  ;
            ByteBuffer buffer = ByteBuffer.allocate(message.length());
            buffer.put(message.getBytes());

            while (true) {
                channel.send(buffer, group);
                System.out.println("Sent the multicast message: " + message);
                buffer.clear();

                buffer.mark();
                System.out.print("Sent: [");
                StringBuilder msg = new StringBuilder();
                while (buffer.hasRemaining()) {
                    msg.append((char) buffer.get());
                }
                System.out.println(msg + "]");
                buffer.reset();

                Thread.sleep(1000);
            }

        } catch (IOException | InterruptedException ex) {
            System.out.println("IOException | InterruptedException: "+ ex.getMessage());
        }
    }
}
