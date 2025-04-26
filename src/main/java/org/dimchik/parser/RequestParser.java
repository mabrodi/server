package org.dimchik.parser;

import org.dimchik.enums.HttpMethod;
import org.dimchik.entity.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {
    private final Request request;

    public RequestParser() {
        this.request = new Request();
    }

    public Request parseRequest(BufferedReader reader) throws IOException {
        injectUriAndMethod(reader);
        injectHeaders(reader);

        return request;
    }

    private void injectUriAndMethod(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            throw new IOException("Empty request line");
        }

        String[] requestLineParams = requestLine.split(" ");
        if (requestLineParams.length < 2) {
            throw new IOException("Invalid request line format");
        }

        try {
            request.setHttpMethod(HttpMethod.valueOf(requestLineParams[0]));
        } catch (IllegalArgumentException e) {
            throw new IOException("Unsupported HTTP method: " + requestLineParams[0]);
        }

        request.setUri(requestLineParams[1]);
    }

    private void injectHeaders(BufferedReader reader) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String headerLine;

        while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
            String[] parts = headerLine.split(":", 2);
            if (parts.length != 2) {
                throw new IOException("Invalid header format: " + headerLine);
            }
            headers.put(parts[0].trim(), parts[1].trim());
        }

        request.setHeaders(headers);
    }
}
