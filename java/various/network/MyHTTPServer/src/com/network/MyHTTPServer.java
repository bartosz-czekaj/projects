package com.network;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MyHTTPServer {
    public static void Establish() throws IOException {
        System.out.println("MyHTTPServer Started");
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/index", new DetailHandler());
        server.start();
    }
}
