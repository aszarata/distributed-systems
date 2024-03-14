package server;

import java.net.Socket;

import java.util.HashMap;


public class Server {

    int portNumber;
    String hostName;
    Logger logger = new Logger();

    private HashMap<Integer, Socket> clients = new HashMap<>();

    public Server(int portNumber, String hostName) {
        this.portNumber = portNumber;
        this.hostName = hostName;
    }

    public void run() {

        logger.start();
        try {
        
            // create tcp and udp chanel
            ServerTcpChannel tcpChannel = new ServerTcpChannel(this);
            ServerUdpChannel udpChannel = new ServerUdpChannel(this);

            Thread tcp = new Thread(tcpChannel);
            Thread udp = new Thread(udpChannel);

            tcp.start();
            udp.start();

            tcp.join();
            udp.join();


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int addClient(Socket socket) {

        int clientLocalPort = socket.getPort();
        clients.put(clientLocalPort, socket);

        
        return clientLocalPort;
    }

    public HashMap<Integer, Socket> getClients() {
        return clients;
    }

    public void removeClient(int clientID) {
        clients.remove(clientID);
    }

    public int getPortNumber() {
        return portNumber;
    }

    public Logger getLogger() {
        return logger;
    }
}
