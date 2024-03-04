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
                System.out.println("client connected " + 
                                    clientSocket.getInetAddress() 
                                                .getHostAddress());


                // handle client and create thread
                clients.put(clientsNum, clientSocket);
                ClientHandler client = new ClientHandler(clientSocket);

                new Thread(client).start();

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



    private static class ClientHandler implements Runnable {

        private final Socket clientSocket;

        public ClientHandler(Socket socket){
            clientSocket = socket;
        }


        @Override
        public void run() {
            try (// in & out streams
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // read msg, send response
                String msg = in.readLine();
                System.out.println("received msg: " + msg);
                out.println("Pong Java Tcp");
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
