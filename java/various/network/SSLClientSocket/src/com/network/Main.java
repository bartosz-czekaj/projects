package com.network;

public class Main {

    public static void main(String[] args) {
	// write your code here
        try {
            SSLClientSocket.Established();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
