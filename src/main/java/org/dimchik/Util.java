package org.dimchik;

import org.dimchik.request.RequestTwo;

import java.io.BufferedReader;
import java.io.IOException;

public class Util {
    public RequestTwo createRequest() throws IOException {
        BufferedReader bufferedReader = null;
        RequestTwo requestTwo = new RequestTwo(bufferedReader, "GET", "/", "HTTP/1.1");

        requestTwo.
    }
}
