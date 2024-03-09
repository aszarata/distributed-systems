package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTcpChannel implements ClientChannel{
    // connection with server
    int serverPortNumber;
    String hostName;

    Socket socket = null;

    // menager for the content of messages
    MessageMenager messageMenager = new MessageMenager();

    // bool for interrupting the thread
    private boolean running = true;

    public ClientTcpChannel( int serverPortNumber, String hostName ) {
        this.serverPortNumber = serverPortNumber;
        this.hostName = hostName;
    }

    @Override
    public void run() throws InterruptedException {

        MessageHandler messageHandler = new MessageHandler();

        Thread messageThread = new Thread(messageHandler);

        messageThread.start();

    }

    @Override
    public void close() {
        running = false; // ending the threads  
        try {          
            socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void createSocket() {
        try {
            socket = new Socket(hostName, serverPortNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessageToServer() {

        try {

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(messageMenager.tcpMessage());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private class MessageHandler implements Runnable {

        @Override
        public void run() {
            try {

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while(running){
                    String response = in.readLine();

                    if(response != null){
                        System.out.println(response);
                    }
                }
            } catch (IOException e) {
                System.out.println("TCP Channel Closed");
                // e.printStackTrace();
            }
        }

    }

    public int getLocalPort(){
        return socket.getLocalPort();
    }
    
}
