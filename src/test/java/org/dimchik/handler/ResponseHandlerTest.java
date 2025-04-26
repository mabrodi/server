package org.dimchik.handler;

import org.dimchik.entity.Request;
import org.dimchik.entity.Response;
import org.dimchik.enums.ContentType;
import org.dimchik.enums.HttpMethod;
import org.dimchik.enums.HttpStatus;
import org.dimchik.io.reader.ContentReader;
import org.dimchik.io.reader.FileContentReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ResponseHandlerTest {

    @Test
    public void testReturnRedirectForUri() throws IOException {
        Request request = new Request();
        request.setUri("/");

        FileContentReader fileContentReader = new FileContentReader("", "");
        ResponseHandler responseHandler = new ResponseHandler(fileContentReader);
        Response response = responseHandler.processRequest(request);

        assertEquals(HttpStatus.MOVED_PERMANENTLY, response.getHttpStatus());
        assertEquals("/index.html", response.getHeaders().get("Location"));
    }

    @Test
    public void testReturnNotFound() throws IOException {
        Request request = new Request();
        request.setUri("/tttt.html");

        FileContentReader fileContentReader = new FileContentReader("", "");
        ResponseHandler responseHandler = new ResponseHandler(fileContentReader);
        Response response = responseHandler.processRequest(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getHttpStatus());
    }

}