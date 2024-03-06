package client;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        String hostName = "localhost";
        int portNumber = 12345;

        Client client = new Client(portNumber, hostName);

        client.run();
    }
}
