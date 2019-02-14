package com.network;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            MyHTTPServer.Establish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
