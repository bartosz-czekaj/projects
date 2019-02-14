package com.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

public class Main {

    public static void main(String[] args) throws SocketException {

        final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

        Collections.list(networkInterfaces).stream().map(NetworkInterfaceData::getMACIdentifier).filter(mac->mac.compareTo("---") != 0 ).forEach(System.out::println);


        NetworkInterfaceData.ShowNetworkInterface();

    }
}
