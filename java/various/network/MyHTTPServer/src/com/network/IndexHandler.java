package com.network;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class IndexHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println(exchange.getRemoteAddress());
        String response = getResponse();
        exchange.sendResponseHeaders(200, response.length());
        OutputStream out = exchange.getResponseBody();
        out.write(response.toString().getBytes());
        out.close();
    }

    private String getResponse() {
        StringBuilder responseBuffer = new StringBuilder();
        responseBuffer
                .append("<html><h1>HTTPServer Home Page.... </h1><br>")
                .append("<b>Welcome to the new and improved web server!</b><BR>")
                .append("</html>");
        return responseBuffer.toString();
    }
}
