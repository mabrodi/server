package org.dimchik.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileHandler {
    private final String basePath;

    public FileHandler(String basePath) {
        this.basePath = basePath;
    }

    public File resolve(String uri) {
        return new File(basePath + uri).getAbsoluteFile();
    }

    public boolean exists(File file) {
        return file.exists() && file.isFile();
    }

    public byte[] readFile(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }
}
