package server;

import java.net.Socket;

import java.util.HashMap;


public class Server {

    int portNumber;
    String hostName;
    Logger logger = new Logger();

    private HashMap<Integer, Socket> clients = new HashMap<>();
    private int clientsNum = 0;

    public Server(int portNumber, String hostName) {
        this.portNumber = portNumber;
        this.hostName = hostName;
    }

    public void run() {

        logger.start();
        try {
        
            // create tcp chanel
            ServerTcpChannel tcpChannel = new ServerTcpChannel(this);

            Thread tcp = new Thread(tcpChannel);

            tcp.start();

        
            tcp.join();


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int addClient(Socket socket) {
        
        clients.put(clientsNum, socket);
        clientsNum++;
        
        return clientsNum - 1;
    }

    public HashMap<Integer, Socket> getClients() {
        return clients;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public Logger getLogger() {
        return logger;
    }

}
