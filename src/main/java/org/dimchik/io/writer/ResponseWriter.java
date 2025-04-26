package org.dimchik.io.writer;

import org.dimchik.entity.Response;

import java.io.BufferedWriter;
import java.io.IOException;

public interface ResponseWriter {
    void writeResponse(Response response, BufferedWriter writer) throws IOException;

    void writeErrorResponse(Response response, BufferedWriter writer) throws IOException;
}
