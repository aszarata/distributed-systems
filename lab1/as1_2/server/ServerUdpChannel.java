package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;


public class ServerUdpChannel implements Runnable {


    private int portNumber;
    static Server server;
    
    DatagramSocket serverUdpSocket = null;

    Logger logger;
    
    public ServerUdpChannel(Server mainServer){
        server = mainServer;
        portNumber = server.getPortNumber();
        logger = server.getLogger();
    }

    @Override
    public void run() {

        logger.confirm("UDP channel");

        try {
            // create socket
            serverUdpSocket = new DatagramSocket(portNumber);
            byte[] receiveBuffer = new byte[1024];
            
            // handle udp message
            while(true) {

                Arrays.fill(receiveBuffer, (byte)0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

                serverUdpSocket.receive(receivePacket);
    
                String msg = new String(receivePacket.getData(), 0, receivePacket.getLength());
                logger.displayMssage(msg, portNumber);


                // confirmation
                byte[] confirmationBuffer = logger.udpReply().getBytes();

                DatagramPacket confirmationPacket = new DatagramPacket(confirmationBuffer, confirmationBuffer.length,
                            receivePacket.getAddress(), receivePacket.getPort());
                    serverUdpSocket.send(confirmationPacket);
                
                // send to other clients
                int senderID = receivePacket.getPort();
                handleUdpMessage( senderID, msg );
            }   

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // send UDP message to all clients
    private void handleUdpMessage( int senderID, String message){
        byte[] confirmationBuffer = logger.directedMessage(message, senderID).getBytes();
        
        HashMap<Integer, Socket> clients = server.getClients();

        for (Integer key : clients.keySet()) {
            if (!key.equals(senderID)){
                try {
                    // send message to other clients
                    DatagramPacket confirmationPacket = new DatagramPacket(confirmationBuffer, confirmationBuffer.length,
                                    clients.get(key).getInetAddress(), clients.get(key).getPort());
                
                    serverUdpSocket.send(confirmationPacket); 

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}
