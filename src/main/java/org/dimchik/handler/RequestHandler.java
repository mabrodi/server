package org.dimchik.handler;

import org.dimchik.enums.ContentType;
import org.dimchik.enums.HttpStatus;
import org.dimchik.parser.RequestParser;
import org.dimchik.entity.Request;
import org.dimchik.entity.Response;
import org.dimchik.io.reader.FileContentReader;
import org.dimchik.io.writer.BufferedResponseWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class RequestHandler {
    private static final String MAIN_PAGE = "/index.html";

    private final RequestParser requestParser;
    private final FileContentReader contentReader;
    private final BufferedResponseWriter responseWriter;
    private final BufferedReader bufferedReader;
    private final BufferedWriter bufferedWriter;

    public RequestHandler(BufferedReader bufferedReader, BufferedWriter bufferedWriter, FileContentReader contentReader) {
        this.bufferedReader = bufferedReader;
        this.bufferedWriter = bufferedWriter;
        this.contentReader = contentReader;
        requestParser = new RequestParser();
        responseWriter = new BufferedResponseWriter();
    }

    public void handle() throws IOException {
        try {
            Request request = requestParser.parseRequest(bufferedReader);
            processRequest(request);
        } catch (IOException e) {
            handleError(e);
        }
    }


    private void processRequest(Request request) throws IOException {
        if ("/".equals(request.getUri())) {
            Response redirectResponse = createRedirectResponse();
            responseWriter.writeResponse(redirectResponse, bufferedWriter);
            return;
        }

        byte[] content = contentReader.readContent(request.getUri());
        Response response = content != null
                ? createSuccessResponse(content, request.getUri())
                : createNotFoundResponse();

        responseWriter.writeResponse(response, bufferedWriter);
    }

    private Response createRedirectResponse() {
        Response response = new Response();
        response.setHttpStatus(HttpStatus.MOVED_PERMANENTLY);
        response.addHeader("Location", MAIN_PAGE);
        return response;
    }

    private Response createSuccessResponse(byte[] content, String uri) {
        Response response = new Response();
        response.setHttpStatus(HttpStatus.OK);
        response.setContent(new String(content));
        response.addHeader("Content-Type", determineContentType(uri));
        return response;
    }

    private Response createNotFoundResponse() throws IOException {
        byte[] errorContent = contentReader.readErrorTemplate(HttpStatus.NOT_FOUND);
        Response response = new Response();
        response.setHttpStatus(HttpStatus.NOT_FOUND);
        if (errorContent != null) {
            response.setContent(new String(errorContent));
            response.addHeader("Content-Type", "text/html");
        }
        return response;
    }


    private void handleError(IOException e) throws IOException {
        Response response = new Response();
        HttpStatus status = determineErrorStatus(e);
        response.setHttpStatus(status);

        byte[] contentError = contentReader.readErrorTemplate(status);
        if (contentError != null) {
            response.setContent(new String(contentError));
            response.addHeader("Content-Type", "text/html");
        } else {
            response.setContent(e.getMessage());
        }

        responseWriter.writeErrorResponse(response, bufferedWriter);
    }

    private HttpStatus determineErrorStatus(IOException e) {
        String message = e.getMessage();
        if (message.contains("Empty request") || message.contains("Invalid request line")) {
            return HttpStatus.BAD_REQUEST;
        } else if (message.contains("Unsupported HTTP method")) {
            return HttpStatus.METHOD_NOT_ALLOWED;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    private String determineContentType(String uri) {
        return ContentType.fromFileName(uri).getMimeType();
    }
}
