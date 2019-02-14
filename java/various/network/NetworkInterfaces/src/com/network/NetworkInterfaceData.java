package com.network;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

public class NetworkInterfaceData {
    public static void ShowNetworkInterface() {
        try {
            Enumeration<NetworkInterface> interfaceEnum = NetworkInterface.getNetworkInterfaces();
            System.out.printf("Name      Display name\n");

            Collections.list(interfaceEnum)
                    .stream()
                    .forEach(
                            (element) -> {
                                System.out.printf("%-8s  %-32s\n", element.getName(), element.getDisplayName());
                                System.out.println("MAC: " + getMACIdentifier(element));

                                }

                    );
        }catch (SocketException ex) {
            System.out.println("SocketException: " + ex.getMessage());
        }
    }

    public static String getMACIdentifier(NetworkInterface network) {
        StringBuilder identifier = new StringBuilder();
        try {
            byte[] macBuffer = network.getHardwareAddress();
            if (macBuffer != null) {
                for (int i = 0; i < macBuffer.length; i++) {
                    identifier.append(
                            String.format("%02X%s",macBuffer[i],
                                    (i < macBuffer.length - 1) ? "-" : ""));
                }
            } else {
                return "---";
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return identifier.toString();
    }
}