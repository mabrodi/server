package org.dimchik.enums;

public enum HttpStatus {
    OK("OK", 200),
    MOVED_PERMANENTLY("MOVED_PERMANENTLY", 301),
    BAD_REQUEST("Bad Request", 400),
    NOT_FOUND("Not found", 404),
    METHOD_NOT_ALLOWED("Method Not Allowed", 405),
    INTERNAL_SERVER_ERROR("Internal Server Error", 500);

    private final String description;
    private final int code;

    HttpStatus(String description, int code) {
        this.description = description;
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }
}
