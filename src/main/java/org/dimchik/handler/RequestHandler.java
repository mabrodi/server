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
    private final RequestParser requestParser;
    private final BufferedReader bufferedReader;
    private final BufferedWriter bufferedWriter;
    private final ResponseHandler responseHandler;
    private final BufferedResponseWriter responseWriter;

    public RequestHandler(BufferedReader bufferedReader, BufferedWriter bufferedWriter, FileContentReader fileContentReader) {
        this.bufferedReader = bufferedReader;
        this.bufferedWriter = bufferedWriter;
        requestParser = new RequestParser();
        responseWriter = new BufferedResponseWriter();
        responseHandler = new ResponseHandler(fileContentReader);
    }

    public void handle() throws IOException {
        try {
            Request request = requestParser.parseRequest(bufferedReader);

            Response response = responseHandler.processRequest(request);
            responseWriter.writeResponse(response, bufferedWriter);

        } catch (IOException e) {
            Response response = responseHandler.handleError(e);
            responseWriter.writeErrorResponse(response, bufferedWriter);
        }
    }

}
