package client;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.net.MulticastSocket;



public class ClientUdpChannel implements ClientChannel{
    // connection with server
    int serverPortNumber;
    int localPortNumber;

    DatagramSocket socket = null;

    // multicast
    int multicastPortNumber;
    String MULTICAST_ADDRESS = null;
    
    MulticastSocket multicastSocket = null;
    InetAddress group = null;

     // menager for the content of messages
     MessageMenager messageMenager = new MessageMenager();

     // bool for interrupting the thread
     private boolean running = true;


    public ClientUdpChannel( int serverPortNumber, int localPortNumber, int multicastPortNumber, String multicastAddress ) {
        this.serverPortNumber = serverPortNumber;
        this.localPortNumber = localPortNumber;

        this.multicastPortNumber = multicastPortNumber;
        MULTICAST_ADDRESS = multicastAddress;
    }

    
    // start threads for message recieve
    @Override
    public void run() throws InterruptedException {
        
        MessageHandler messageHandler = new MessageHandler();
        MulticastMessageHandler multicastMessageHandler = new MulticastMessageHandler();

        Thread messageThread = new Thread(messageHandler);
        Thread multicastThread = new Thread(multicastMessageHandler);

        messageThread.start();
        multicastThread.start();
        
    }

    @Override
    public void close() {
        running = false; // ending the threads

        // close udp sockets
        socket.close();
        multicastSocket.close();
        
    }


    // create udp and udp multicast socket
    @SuppressWarnings("deprecation")
    @Override
    public void createSocket(){
        try {
            // create socket for connection with server
            socket = new DatagramSocket(localPortNumber);
            
            // create multicast socket and group
            multicastSocket = new MulticastSocket(multicastPortNumber);
            group = InetAddress.getByName(MULTICAST_ADDRESS);
            multicastSocket.joinGroup(group);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void sendMessageToServer() {

        try {
            InetAddress address = InetAddress.getByName("localhost");
            byte[] sendBuffer = messageMenager.udpMessage().getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, serverPortNumber);
            socket.send(sendPacket);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    // UDP to server
    // wait for udp message and display it
    private class MessageHandler implements Runnable {

        @Override
        public void run() {
            byte[] receiveBuffer = new byte[1024];

            try {
                while (running){
                    Arrays.fill(receiveBuffer, (byte) 0);
                    DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                
                    socket.receive(receivePacket);

                    String msg = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    System.out.println(msg);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }    
        
    }

    // UDP Multicast

    public void sendMulticastMessage() {
 
        try {
            byte[] sendBuffer = messageMenager.multicastMessage(localPortNumber).getBytes();
            
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, group, multicastPortNumber);
            multicastSocket.send(sendPacket);
            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // wait for multicast message and display it
    private class MulticastMessageHandler implements Runnable {

        @Override
        public void run() {
            byte[] receiveBuffer = new byte[1024];

            try {
                while (running){
                    Arrays.fill(receiveBuffer, (byte) 0);
                    DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                
                    multicastSocket.receive(receivePacket);

                    String msg = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    System.out.println(msg);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }    
        
    }
}
