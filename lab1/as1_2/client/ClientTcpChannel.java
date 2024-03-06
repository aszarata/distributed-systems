package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTcpChannel{

    int portNumber;
    String hostName;

    Socket socket = null;

    public ClientTcpChannel( int portNumber, String hostName ) {
        this.portNumber = portNumber;
        this.hostName = hostName;
    }

    public void run() throws InterruptedException {

        MessageHandler messageHandler = new MessageHandler();

        Thread messageThread = new Thread(messageHandler);

        messageThread.start();
        
        messageThread.join();
        
            

    }

    public void createSocket(){
        try {
            socket = new Socket(hostName, portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToServer() {

        try (
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                out.println("Ping Java Tcp");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public class MessageHandler implements Runnable {

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
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

    
}
