package org.dimchik.request;

import java.io.BufferedReader;
import java.io.IOException;

public class Request {
    private final String method;
    private final String uri;
    private final String protocol;

    Request(BufferedReader bufferedReader) throws IOException {
        String requestLine = bufferedReader.readLine();
        if (requestLine == null) {
            throw new NullPointerException("empty http request");
        }

        String[] requestLineParams = requestLine.split(" ");

        method = requestLineParams[0];
        uri = requestLineParams[1];
        protocol = requestLineParams[2];
    }


    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }
}
