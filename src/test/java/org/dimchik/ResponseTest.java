package org.dimchik;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {
    private final String WEB_APP_PATH = "src/main/resources/webApp/";

    @Test
    public void testSendResponseForIndexHtml() throws IOException {
        StringWriter stringWriter = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(stringWriter);
        BufferedReader bufferedReader = new BufferedReader(new StringReader("GET / HTTP/1.1"));

        Request request = new Request(bufferedReader);
        Response response = new Response(bufferedWriter, WEB_APP_PATH);
        response.sendResponse(request);

        File file = new File(WEB_APP_PATH + "index.html");
        FileInputStream fileStream = new FileInputStream(file);
        byte[] buffer = fileStream.readAllBytes();

        String result = stringWriter.toString();
        assertTrue(result.contains("HTTP/1.1 200 OK"));
        assertTrue(result.contains(new String(buffer)));
    }

    @Test
    public void testSendResponseForNotFound() throws IOException {
        StringWriter stringWriter = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(stringWriter);
        BufferedReader bufferedReader = new BufferedReader(new StringReader("GET /nonexistent.html HTTP/1.1"));

        Request request = new Request(bufferedReader);
        Response response = new Response(bufferedWriter, WEB_APP_PATH);
        response.sendResponse(request);

        File file = new File(WEB_APP_PATH + "404.html");
        FileInputStream fileStream = new FileInputStream(file);
        byte[] buffer = fileStream.readAllBytes();

        String result = stringWriter.toString();
        assertTrue(result.contains("HTTP/1.1 404 Not Found"));
        assertTrue(result.contains(new String(buffer)));
    }

    @Test
    public void testSendResponseForRedirect() throws IOException {
        StringWriter stringWriter = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(stringWriter);
        BufferedReader bufferedReader = new BufferedReader(new StringReader("GET /index.html HTTP/1.1"));

        Request request = new Request(bufferedReader);
        Response response = new Response(bufferedWriter, WEB_APP_PATH);
        response.sendResponse(request);


        String result = stringWriter.toString();
        assertTrue(result.contains("HTTP/1.1 301 Moved Permanently"));
    }
}