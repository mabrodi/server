package org.dimchik;

import org.dimchik.request.Request;
import org.dimchik.response.Response;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port = 3000;
    private String webAppPath = "/";

    public void setPort(int port) {
        this.port = port;
    }

    public void setWebAppPath(String webAppPath) {
        this.webAppPath = webAppPath;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                ) {
                    Request request = new Request(reader);
                    Response response = new Response(writer, webAppPath);
                    response.sendResponse(request);
                }
            }
        }
    }
}