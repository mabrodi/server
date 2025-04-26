package org.dimchik.io.reader;

import org.dimchik.enums.HttpStatus;

import java.io.IOException;

public interface ContentReader {
    byte[] readContent(String uri) throws IOException;

    byte[] readErrorTemplate(HttpStatus status) throws IOException;
}
