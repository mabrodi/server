package org.dimchik;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Response {
    private final BufferedWriter writer;
    private final String webAppPath;

    Response(BufferedWriter writer, String webAppPath) {
        this.writer = writer;
        this.webAppPath = webAppPath;
    }

    public void sendResponse(Request request) throws IOException {
        String resourcePath = request.getUri().replaceAll("//+", "/");

        writer.write(request.getProtocol() + " ");
        if ("/index.html".equals(resourcePath)) {
            sendRedirect("/");
            return;
        }

        if ("/".equals(resourcePath)) {
            resourcePath = "/index.html";
        }

        File file = new File(webAppPath + resourcePath);

        if (!file.exists() || !file.isFile()) {
            file = new File(webAppPath + "/404.html");
            if (!file.exists() || !file.isFile()) {
                sendNotFoundResponse();
                return;
            } else {
                sendFileResponse(file, 404);
            }
        }

        sendFileResponse(file, 200);
    }

    private void sendRedirect(String location) throws IOException {
        writer.write("301 Moved Permanently\r\n");
        writer.write("Location: " + location + "\r\n\r\n");
        writer.flush();
    }

    private void sendFileResponse(File file, int statusCode) throws IOException {
        String statusMessage = statusCode == 200 ? "OK" : "Not Found";
        writer.write(statusCode + " " + statusMessage + "\r\n");
        writer.write("Content-Length: " + file.length() + "\r\n");
        writer.write("Content-Type: text/html\r\n\r\n");

        try (FileInputStream fileStream = new FileInputStream(file)) {
            byte[] buffer = fileStream.readAllBytes();
            writer.write(new String(buffer));
        }
        writer.flush();
    }

    private void sendNotFoundResponse() throws IOException {
        writer.write("404 Not Found\r\n");
        writer.write("Content-Type: text/html\r\n\r\n");
        writer.write("<h1>404 - Page Not Found</h1>");
        writer.flush();
    }
}
