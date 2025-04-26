package org.dimchik.entity;

import org.dimchik.enums.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class Response {
    private HttpStatus httpStatus;
    private String content;
    private final Map<String, String> headers;

    public Response() {
        this.headers = new HashMap<>();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void addHeader(String name, String value) {
        this.headers.put(name, value);
    }
}
