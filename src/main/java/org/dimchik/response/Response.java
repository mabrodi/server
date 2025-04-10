package org.dimchik.response;

import org.dimchik.HttpStatus;
import org.dimchik.request.Request;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

public class Response {
    private final ResponseWriter responseWriter;
    private final FileHandler fileHandler;

    Response(BufferedWriter writer, String webAppPath) {
        this.responseWriter = new ResponseWriter(writer);
        this.fileHandler = new FileHandler(webAppPath);
    }

    public void sendResponse(Request request) throws IOException {
        String uri = request.getUri().replaceAll("//+", "/");

        if (uri.equals("/index.html")) {
            responseWriter.writeRedirect("/");
            return;
        }

        File file = fileHandler.resolve(uri);
        if (!fileHandler.exists(file)) {
            File notFoundFile = fileHandler.resolve("/404.html");
            if (fileHandler.exists(notFoundFile)) {
                byte[] content = fileHandler.readFile(notFoundFile);
                responseWriter.writeResponse(HttpStatus.NOT_FOUND, content);
            } else {
                responseWriter.writeNotFound();
            }

            return;
        }

        byte[] content = fileHandler.readFile(file);
        responseWriter.writeResponse(HttpStatus.OK, content);
    }
}
