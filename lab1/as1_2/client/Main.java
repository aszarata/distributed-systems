package client;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        String hostName = "localhost";
        int portNumber = 12345;
        int multicastPortNumber = 50000;
        String MULTICAST_ADDRESS = "230.0.0.0";

        Client client = new Client(portNumber, hostName, multicastPortNumber, MULTICAST_ADDRESS);

        client.run();
    }
}
