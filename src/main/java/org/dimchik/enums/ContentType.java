package org.dimchik.enums;

public enum ContentType {
    HTML("text/html", "html", "htm"),
    CSS("text/css", "css"),
    JS("application/javascript", "js"),
    PNG("image/png", "png"),
    JPEG("image/jpeg", "jpg", "jpeg"),
    PLAIN_TEXT("text/plain", "txt");

    private final String mimeType;
    private final String[] fileExtensions;

    ContentType(String mimeType, String... fileExtensions) {
        this.mimeType = mimeType;
        this.fileExtensions = fileExtensions;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static ContentType fromFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return HTML;
        }

        String lowerCaseFileName = fileName.toLowerCase();
        for (ContentType type : values()) {
            for (String ext : type.fileExtensions) {
                if (lowerCaseFileName.endsWith("." + ext)) {
                    return type;
                }
            }
        }

        return HTML;
    }
}
