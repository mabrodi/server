package org.dimchik;

import java.io.BufferedWriter;
import java.io.IOException;

public class ResponseWriter {
    private final BufferedWriter writer;

    public ResponseWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    public void writeResponse(String status, byte[] body) throws IOException {
        writer.write("HTTP/1.1 " + status);
        writer.newLine();
        writer.newLine();
        writer.write(new String(body));
        writer.flush();
    }

    public void writeNotFound() throws IOException {
        writeResponse("404 Not Found", "<h1>404 - Page Not Found</h1>".getBytes());
    }

    public void writeRedirect(String location) throws IOException {
        writer.write("HTTP/1.1 301 Moved Permanently");
        writer.newLine();
        writer.write("Location: " + location);
        writer.newLine();
        writer.newLine();
        writer.flush();
    }
}
