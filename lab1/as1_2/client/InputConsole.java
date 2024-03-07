package client;

import java.util.Scanner;

public class InputConsole implements Runnable{
    
    String userPrefix = "";

    Scanner scanner = new Scanner(System.in);
    char input;

    ClientTcpChannel tcpChannel;
    ClientUdpChannel udpChannel;

    public InputConsole(ClientTcpChannel tcpChannel, ClientUdpChannel udpChannel) {
        this.tcpChannel = tcpChannel;
        this.udpChannel = udpChannel;
    }

    @Override
    public void run() {

        System.out.println("CLIENT\n");
        do {
            System.out.print(userPrefix);
            input = scanner.next().charAt(0);

            switch (input) {
                // TCP communication
                case 't':
                    tcpChannel.sendMessageToServer();
                    break;
                
                // UDP communication
                case 'u':
                    udpChannel.sendMessageToServer();
                    break;

                // Multicast communication
                case 'm':
                    udpChannel.sendMulticastMessage();
                    break;

                // Exit
                case 'e':
                    break;
            
                default:
                    System.out.println("Invalid character. Try using:\nt - TCP message to server\nu - UDP message to server\nm - multicast UDP message to other clients\ne - exit.");
                    break;
            }

        } while (input != 'e');
    }


}
