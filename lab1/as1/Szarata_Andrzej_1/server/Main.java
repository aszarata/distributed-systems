package server;


import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        int portNumber = 12345;
        String hostName = "localhost";

        Server server = new Server(portNumber, hostName);

        server.run();
    }
}
