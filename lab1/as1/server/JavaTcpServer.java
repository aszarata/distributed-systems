import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class JavaTcpServer {


    static HashMap<Integer, Socket> clients = new HashMap<>();
    private static int clientsNum = 0;
    

    // main
    public static void main(String[] args) throws IOException {

        System.out.println("JAVA TCP SERVER");
        int portNumber = 12345;
        ServerSocket serverSocket = null;
    
        try {
            // create socket
            serverSocket = new ServerSocket(portNumber);

            while(true){

                // accept client
                Socket clientSocket = serverSocket.accept();
                System.out.println("client" + clientsNum + " connected " + 
                                    clientSocket.getInetAddress() 
                                                .getHostAddress());

                // handle client and create thread
                clients.put(clientsNum, clientSocket);
                ClientHandler client = new ClientHandler(clientSocket, clientsNum);

                new Thread(client).start();

                // increase the client incex
                clientsNum++;

                

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if (serverSocket != null){
                serverSocket.close();
            }
        }
    }
    // Send TCP message one to all
    private static void handleTcpMessage(int senderID, String message) throws IOException{

        for (Integer key : clients.keySet()){
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
    private static class ClientHandler implements Runnable {

        private final Socket clientSocket;
        private int clientIndex;

        public ClientHandler(Socket socket, int index){
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
                        System.out.println("[CLIENT " + clientIndex + "] " +  msg);
                        out.println("[SERVER] Pong Java Tcp");

                        handleTcpMessage(clientIndex, msg);
                    } else {
                        break;
                    }
                }
                
                System.out.println("[CLIENT " + clientIndex + "] socked closed");


            } catch (IOException e) {
                // isSocketClosed = true;
                // 
                e.printStackTrace();
            }
        }

    }

}
