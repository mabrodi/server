package org.dimchik.response;

import org.dimchik.HttpStatus;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResponseWriter {
    private final BufferedWriter writer;

    public ResponseWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    public void writeResponse(String status, byte[] body) throws IOException {
        write(status, null, body);
    }

    public void writeNotFound() throws IOException {
        write(HttpStatus.NOT_FOUND, null, "<h1>404 - Page Not Found</h1>".getBytes());
    }

    public void writeRedirect(String location) throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Location", location);

        write(HttpStatus.REDIRECT, headers, null);
    }

    private void write(String status, Map<String, String> headers, byte[] body) throws IOException {
        writer.write("HTTP/1.1 " + status);
        writer.newLine();

        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                writer.write(header.getKey() + ": " + header.getValue());
                writer.newLine();
            }
        }

        writer.newLine();

        if (body != null) {
            writer.write(new String(body));
        }

        writer.flush();
    }
}