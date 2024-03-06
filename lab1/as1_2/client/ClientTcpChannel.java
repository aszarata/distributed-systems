package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientTcpChannel implements Runnable {

    int portNumber;
    String hostName;

    Socket socket = null;

    public ClientTcpChannel( int portNumber, String hostName ) {
        this.portNumber = portNumber;
        this.hostName = hostName;
    }
    @Override
    public void run() {
        try {
            socket = new Socket(hostName, portNumber);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
            while(true){
                String response = in.readLine();
                if(response != null){
                    System.out.println("\n" + response);
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}
