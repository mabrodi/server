package org.dimchik;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class RequestTest {
    @Test
    void testRequestParsing() throws IOException {
        String requestData = "GET /index.html HTTP/1.1";
        BufferedReader reader = new BufferedReader(new StringReader(requestData));

        Request request = new Request(reader);

        assertEquals("GET", request.getMethod());
        assertEquals("/index.html", request.getUri());
        assertEquals("HTTP/1.1", request.getProtocol());
    }

    @Test
    void testEmptyRequest() {
        String requestData = "";
        BufferedReader reader = new BufferedReader(new StringReader(requestData));

        assertThrows(NullPointerException.class, () -> new Request(reader));
    }

    @Test
    void testRequestWithMultipleSlashes() throws IOException {
        String requestData = "GET //path//to//file.html HTTP/1.1";
        BufferedReader reader = new BufferedReader(new StringReader(requestData));

        Request request = new Request(reader);

        assertEquals("//path//to//file.html", request.getUri());
        assertEquals("GET", request.getMethod());
        assertEquals("HTTP/1.1", request.getProtocol());
    }
}