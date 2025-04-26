package org.dimchik.io.reader;

import org.dimchik.enums.HttpStatus;
import org.dimchik.io.FileHandler;

import java.io.File;
import java.io.IOException;

public class FileContentReader implements ContentReader {
    private final FileHandler errorFileHandler;
    private final FileHandler fileHandler;

    public FileContentReader(String basePath, String errorTemplatesPath) {
        this.fileHandler = new FileHandler(basePath);
        this.errorFileHandler = new FileHandler(errorTemplatesPath);
    }

    @Override
    public byte[] readContent(String uri) throws IOException {
        File file = fileHandler.resolve(uri);
        if (fileHandler.exists(file)) {
            return fileHandler.readFile(file);
        }
        return null;
    }

    @Override
    public byte[] readErrorTemplate(HttpStatus status) throws IOException {
        File file = errorFileHandler.resolve("/" + status.getCode() + ".html");
        if (errorFileHandler.exists(file)) {
            return errorFileHandler.readFile(file);
        }

        return null;
    }
}
