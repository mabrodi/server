package org.dimchik;

import org.dimchik.io.reader.FileContentReader;
import org.dimchik.handler.RequestHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port = 3000;
    private FileContentReader contentReader;

    public void setPort(int port) {
        this.port = port;
    }

    public void setWebAppPath(String webAppPath) {
        contentReader = new FileContentReader(webAppPath, webAppPath+"/error");
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                ) {
                    RequestHandler requestHandler = new RequestHandler(reader, writer, contentReader);
                    requestHandler.handle();
                }
            }
        }
    }
}