package org.dimchik;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Response {
    private static final String STATUS_NOT_FOUND = "404 Not Found";
    private static final String STATUS_OK = "200 OK";
    private static final String STATUS_REDIRECT = "301 Moved Permanently";

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
                sendFileResponse(file, STATUS_NOT_FOUND);
            }
        }

        sendFileResponse(file, STATUS_OK);
    }

    private void sendRedirect(String location) throws IOException {
        writer.write(STATUS_REDIRECT);
        writer.newLine();
        writer.write("Location: " + location);
        writer.newLine();
        writer.newLine();
        writer.flush();
    }

    private void sendFileResponse(File file, String status) throws IOException {
        writer.write(status);
        writer.newLine();
        writer.write("Content-Type: text/html");
        writer.newLine();
        writer.newLine();

        try (FileInputStream fileStream = new FileInputStream(file)) {
            byte[] buffer = fileStream.readAllBytes();
            writer.write(new String(buffer));
        }
        writer.flush();
    }

    private void sendNotFoundResponse() throws IOException {
        writer.write(STATUS_NOT_FOUND);
        writer.newLine();
        writer.write("Content-Type: text/html");
        writer.newLine();
        writer.newLine();
        writer.write("<h1>404 - Page Not Found</h1>");
        writer.flush();
    }
}
