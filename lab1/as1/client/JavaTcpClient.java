import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class JavaTcpClient {

    public static void main(String[] args) throws IOException {

        System.out.println("JAVA TCP CLIENT");
        String hostName = "localhost";
        int portNumber = 12345;
        Socket socket = null;
        

        try {
            // create socket
            socket = new Socket(hostName, portNumber);

            // handle client threads
            inputHandler inputing = new inputHandler(socket);
            messageHandler message = new messageHandler(socket);


            Thread inputThread = new Thread(inputing);
            Thread messageThread = new Thread(message);

            inputThread.start();
            messageThread.start();

            inputThread.join();
            messageThread.join();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null){
                socket.close();
            }
        }
    }

    private static class inputHandler implements Runnable{

        Socket socket;
        Scanner scanner = new Scanner(System.in);
        char input;

        public inputHandler(Socket socket) throws IOException{
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                // in & out streams
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                
                // choose client option
                do {
                    System.out.print(">>");
                    input = scanner.next().charAt(0);

                    switch (input) {
                        // TCP communication
                        case 't':
                            // send msg, read response
                            out.println("Ping Java Tcp");
                            break;
                        
                        // UDP communication
                        case 'u':
                            System.out.println("u");
                            break;

                        // Multicast communication
                        case 'm':
                            System.out.println("m");
                            break;

                        // Exit
                        case 'e':
                            System.out.println("e");
                            break;
                    
                        default:
                            System.out.println("Error");
                            break;
                    }

                } while (input != 'e');
            } catch (IOException e) {
                e.printStackTrace();
            }
            scanner.close();
        }

    }

    private static class messageHandler implements Runnable{

        Socket socket;

        public messageHandler(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                
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
