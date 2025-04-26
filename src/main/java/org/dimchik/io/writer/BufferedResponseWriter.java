package org.dimchik.io.writer;

import org.dimchik.entity.Response;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

public class BufferedResponseWriter implements ResponseWriter {
    @Override
    public void writeResponse(Response response, BufferedWriter writer) throws IOException {
        writeStatusLine(response, writer);
        writeHeaders(response, writer);
        writeBody(response, writer);
        writer.flush();
    }

    @Override
    public void writeErrorResponse(Response response, BufferedWriter writer) throws IOException {
        writeStatusLine(response, writer);
        writeHeaders(response, writer);
        writeErrorBody(response, writer);
        writer.flush();
    }

    private void writeStatusLine(Response response, BufferedWriter writer) throws IOException {
        writer.write(String.format("HTTP/1.1 %d %s\r\n",
                response.getHttpStatus().getCode(),
                response.getHttpStatus().getDescription()));
    }

    private void writeHeaders(Response response, BufferedWriter writer) throws IOException {
        if (response.getHeaders() != null) {
            for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
                writer.write(header.getKey() + ": " + header.getValue());
                writer.newLine();
            }
        }

        writer.newLine();
    }

    private void writeBody(Response response, BufferedWriter writer) throws IOException {
        if (response.getContent() != null) {
            writer.write(response.getContent());
        }
    }

    private void writeErrorBody(Response response, BufferedWriter writer) throws IOException {
        if (response.getContent() != null) {
            writer.write(response.getContent());
        } else {
            writer.write("Error: " + response.getHttpStatus().getDescription());
        }
    }
}
