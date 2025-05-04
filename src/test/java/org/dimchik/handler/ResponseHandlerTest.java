package org.dimchik.handler;

import org.dimchik.entity.Request;
import org.dimchik.entity.Response;
import org.dimchik.enums.ContentType;
import org.dimchik.enums.HttpStatus;
import org.dimchik.io.reader.FileContentReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ResponseHandlerTest {

    @Mock
    private FileContentReader fileContentReader;

    @Test
    public void testReturnRedirectForUri() throws IOException {
        Request request = new Request();
        request.setUri("/");

        ResponseHandler responseHandler = new ResponseHandler(fileContentReader);
        Response response = responseHandler.processRequest(request);

        assertEquals(HttpStatus.MOVED_PERMANENTLY, response.getHttpStatus());
        assertEquals("/index.html", response.getHeaders().get("Location"));
    }

    @Test
    public void testReturnNotFoundWhenContentIsNull() throws IOException {
        Request request = new Request();
        request.setUri("/test.html");

        Mockito.when(fileContentReader.readContent("/test.html")).thenReturn(null);
        Mockito.when(fileContentReader.readErrorTemplate(HttpStatus.NOT_FOUND))
                .thenReturn("<html>Error</html>".getBytes());

        ResponseHandler responseHandler = new ResponseHandler(fileContentReader);
        Response response = responseHandler.processRequest(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getHttpStatus());
        assertEquals("<html>Error</html>", response.getContent());
        assertEquals("text/html", response.getHeaders().get("Content-Type"));
    }


    @Test
    public void testReturnSuccessResponseWhenContentExists() throws IOException {
        Request request = new Request();
        request.setUri("/test.html");

        Mockito.when(fileContentReader.readContent("/test.html")).thenReturn("test content".getBytes());

        ResponseHandler responseHandler = new ResponseHandler(fileContentReader);
        Response response = responseHandler.processRequest(request);

        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("test content", response.getContent());
        assertEquals(ContentType.HTML.getMimeType(), response.getHeaders().get("Content-Type"));
    }

    @Test
    public void testHandleBadRequestError() throws IOException {
        IOException exception = new IOException("Empty request");

        Mockito.when(fileContentReader.readErrorTemplate(HttpStatus.BAD_REQUEST)).thenReturn("error content".getBytes());

        ResponseHandler responseHandler = new ResponseHandler(fileContentReader);
        Response response = responseHandler.handleError(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
        assertEquals("error content", response.getContent());
        assertEquals("text/html", response.getHeaders().get("Content-Type"));
    }

    @Test
    public void testHandleInternalServerErrorWhenNoTemplate() throws IOException {
        IOException exception = new IOException("Some error");

        Mockito.when(fileContentReader.readErrorTemplate(HttpStatus.INTERNAL_SERVER_ERROR))
                .thenReturn(null);

        ResponseHandler responseHandler = new ResponseHandler(fileContentReader);
        Response response = responseHandler.handleError(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getHttpStatus());
        assertEquals("Some error", response.getContent());
        assertNull(response.getHeaders().get("Content-Type"));
    }
}