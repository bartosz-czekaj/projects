package com.network;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;

public class DetailHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange)throws IOException {
        Headers requestHeaders = exchange.getRequestHeaders();
        Set<String> keySet = requestHeaders.keySet();
        for (String key : keySet) {
            List values = requestHeaders.get(key);
            String header = key + " = " + values.toString() + "\n";
            System.out.print(header);
        }

        InputStream in = exchange.getRequestBody();
        if (in != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in));) {
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                System.out.println(inputLine);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("Request body is empty");
        }

    }
}
