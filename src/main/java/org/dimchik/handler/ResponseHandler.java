package org.dimchik.handler;

import org.dimchik.entity.Request;
import org.dimchik.entity.Response;
import org.dimchik.enums.ContentType;
import org.dimchik.enums.HttpStatus;
import org.dimchik.io.reader.ContentReader;
import org.dimchik.io.reader.FileContentReader;
import org.dimchik.io.writer.BufferedResponseWriter;
import org.dimchik.io.writer.ResponseWriter;

import java.io.BufferedWriter;
import java.io.IOException;

public class ResponseHandler {
    private static final String MAIN_PAGE = "/index.html";

    private final ContentReader contentReader;

    public ResponseHandler(FileContentReader contentReader) {
        this.contentReader = contentReader;
    }

    public Response processRequest(Request request) throws IOException {
        if ("/".equals(request.getUri())) {
            return createRedirectResponse();
        }

        byte[] content = contentReader.readContent(request.getUri());
        return content != null
                ? createSuccessResponse(content, request.getUri())
                : createNotFoundResponse();

    }

    public Response handleError(IOException e) throws IOException {
        HttpStatus httpStatus = determineErrorStatus(e);
        byte[] errorContent = contentReader.readErrorTemplate(httpStatus);

        Response response = new Response();
        response.setHttpStatus(httpStatus);

        if (errorContent != null) {
            response.setContent(new String(errorContent));
            response.addHeader("Content-Type", "text/html");
        } else {
            response.setContent(e.getMessage());
        }

        return response;
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
        response.addHeader("Content-Type", ContentType.fromFileName(uri).getMimeType());

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

    private HttpStatus determineErrorStatus(IOException e) {
        String message = e.getMessage();
        if (message.contains("Empty request") || message.contains("Invalid request line")) {
            return HttpStatus.BAD_REQUEST;
        } else if (message.contains("Unsupported HTTP method")) {
            return HttpStatus.METHOD_NOT_ALLOWED;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
