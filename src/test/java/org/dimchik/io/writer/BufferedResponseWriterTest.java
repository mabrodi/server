package org.dimchik.io.writer;

import org.dimchik.entity.Response;
import org.dimchik.enums.HttpStatus;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

class BufferedResponseWriterTest {
    @Test
    public void testWriteFullResponse() throws IOException {
        Response response = new Response();
        response.setHttpStatus(HttpStatus.OK);
        response.setContent("Test content");
        response.addHeader("Content-Type", "text/html");
        response.addHeader("Cache-Control", "no-cache");

        StringWriter stringWriter = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(stringWriter);

        BufferedResponseWriter bufferedResponseWriter = new BufferedResponseWriter();
        bufferedResponseWriter.writeResponse(response, bufferedWriter);

        String result = stringWriter.toString();
        assertTrue(result.startsWith("HTTP/1.1 200 OK\r\n"));
        assertTrue(result.contains("Content-Type: text/html"));
        assertTrue(result.contains("Cache-Control: no-cache"));
        assertTrue(result.endsWith("Test content"));
    }

    @Test
    public void testHandleResponseWithoutHeaders() throws IOException {
        Response response = new Response();
        response.setHttpStatus(HttpStatus.BAD_REQUEST);

        StringWriter stringWriter = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(stringWriter);

        BufferedResponseWriter bufferedResponseWriter = new BufferedResponseWriter();
        bufferedResponseWriter.writeResponse(response, bufferedWriter);

        assertEquals("HTTP/1.1 400 Bad Request\r\n\n", stringWriter.toString());
    }

    @Test
    public void testHandleResponseWithoutBody() throws IOException {
        Response response = new Response();
        response.setHttpStatus(HttpStatus.OK);
        response.setContent("<h1>OK</h1>");

        StringWriter stringWriter = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(stringWriter);

        BufferedResponseWriter bufferedResponseWriter = new BufferedResponseWriter();
        bufferedResponseWriter.writeResponse(response, bufferedWriter);

        assertEquals("HTTP/1.1 200 OK\r\n\n<h1>OK</h1>", stringWriter.toString());
    }

    @Test
    public void testHandleErrorResponseWithoutBody() throws IOException {
        Response response = new Response();
        response.setHttpStatus(HttpStatus.NOT_FOUND);

        StringWriter stringWriter = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(stringWriter);

        BufferedResponseWriter bufferedResponseWriter = new BufferedResponseWriter();
        bufferedResponseWriter.writeErrorResponse(response, bufferedWriter);

        assertEquals("HTTP/1.1 404 Not found\r\n\nError: Not found", stringWriter.toString());
    }


}
