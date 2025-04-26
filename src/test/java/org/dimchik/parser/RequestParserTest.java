package org.dimchik.parser;

import org.dimchik.entity.Request;
import org.dimchik.enums.HttpMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RequestParserTest {
    @Test
    public void testParseValidRequest() throws IOException {
        String requestBuilder = "GET /index.html HTTP/1.1\n";
        requestBuilder += "Host: localhost\n";
        requestBuilder += "User-Agent: test-agent\n\n";


        BufferedReader bufferedReader = new BufferedReader(new StringReader(requestBuilder));

        RequestParser requestParser = new RequestParser();
        Request request = requestParser.parseRequest(bufferedReader);

        assertEquals(HttpMethod.GET, request.getHttpMethod());
        assertEquals("/index.html", request.getUri());
        assertEquals(Map.of(
                "Host", "localhost",
                "User-Agent", "test-agent"
        ), request.getHeaders());
    }

    @Test
    public void testThrowOnEmptyRequest() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new StringReader(""));

        RequestParser requestParser = new RequestParser();

        IOException exception = Assertions.assertThrows(IOException.class, () -> {
            requestParser.parseRequest(bufferedReader);
        });

        assertEquals("Empty request line", exception.getMessage());
    }

    @Test
    public void testThrowOnInvalidRequestLineFormat() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new StringReader("GET/index.html"));

        RequestParser requestParser = new RequestParser();

        IOException exception = Assertions.assertThrows(IOException.class, () -> {
            requestParser.parseRequest(bufferedReader);
        });

        assertEquals("Invalid request line format", exception.getMessage());
    }

    @Test
    public void testThrowOnUnsupportedHttpMethod() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new StringReader("PUT /index.html HTTP/1.1\n"));

        RequestParser requestParser = new RequestParser();

        IOException exception = Assertions.assertThrows(IOException.class, () -> {
            requestParser.parseRequest(bufferedReader);
        });

        assertEquals("Unsupported HTTP method: PUT", exception.getMessage());
    }

    @Test
    public void testThrowOnInvalidHeaderFormat() throws IOException {
        String requestBuilder = "GET /index.html HTTP/1.1\n";
        requestBuilder += "Accept-encoding: gzip, deflate, br, zstd\n";
        requestBuilder += "X-Api-Secret\n";
        requestBuilder += "Host: localhost\n";
        requestBuilder += "User-Agent: test-agent\n\n";

        BufferedReader bufferedReader = new BufferedReader(new StringReader(requestBuilder));

        RequestParser requestParser = new RequestParser();

        IOException exception = Assertions.assertThrows(IOException.class, () -> {
            requestParser.parseRequest(bufferedReader);
        });

        System.out.println(exception.getMessage());
        assertEquals("Invalid header format: X-Api-Secret", exception.getMessage());
    }
}