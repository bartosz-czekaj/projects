package com.network;

public class Main {

    public static void main(String[] args) {
        try {
            UDPDatagramMulticastClient.Establish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
