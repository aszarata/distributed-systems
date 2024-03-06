package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class ServerTcpChannel implements Runnable{

    private int portNumber;
    static Server server;
    
    ServerSocket serverTcpSocket = null;

    Logger logger;
    
    public ServerTcpChannel(Server mainServer){
        server = mainServer;
        portNumber = server.getPortNumber();
        this.logger = server.getLogger();
    }


    @Override
    public void run() {

        logger.confirm("TCP channel");
        try {
            // create socket
            serverTcpSocket = new ServerSocket(portNumber);
            
            while (true) {
                
                // accept client
                Socket clientSocket = serverTcpSocket.accept();

                // handle client and create thread
                int clientIndex = server.addClient(clientSocket);
                ClientHandler client = new ClientHandler(clientSocket, clientIndex);

                new Thread(client).start();

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void handleTcpMessage(int senderID, String message) throws IOException{

        HashMap<Integer, Socket> clients = server.getClients();

        for (Integer key : clients.keySet()) {

            if (!key.equals(senderID)) {
                
                Socket clientSocket = clients.get(key);
            
                try (
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                        out.println("[CLIENT " + senderID + "] " + message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            } 
            
        }
    }
    // TCP Thread for the single client
    private class ClientHandler implements Runnable {

        private final Socket clientSocket;
        private int clientIndex;

        public ClientHandler(Socket socket, int index) {
            clientSocket = socket;
            clientIndex = index;
        }

        @Override
        public void run() {
            try (// in & out streams
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // read msg, send response
                while (true) {
                    String msg = in.readLine();
                    
                    if (msg != null) {
                        out.println("[SERVER] Pong Java Tcp");

                        handleTcpMessage(clientIndex, msg);
                    } else {
                        break;
                    }
                }
                
                System.out.println("[CLIENT " + clientIndex + "] socked closed");


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
