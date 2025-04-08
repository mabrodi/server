package org.dimchik;

import java.io.IOException;

public class Started {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.setPort(3000);
        server.setWebAppPath("src/main/resources/webApp");
        server.start();
    }
}
