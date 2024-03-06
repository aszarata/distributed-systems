package client;

import java.util.Scanner;

public class InputConsole implements Runnable{
    
    String userPrefix = ">>";

    Scanner scanner = new Scanner(System.in);
    char input;

    ClientTcpChannel tcpChannel;

    public InputConsole(ClientTcpChannel tcpChannel) {
        this.tcpChannel = tcpChannel;
    }

    @Override
    public void run() {
        
        do {
            System.out.print(">>");
            input = scanner.next().charAt(0);

            switch (input) {
                // TCP communication
                case 't':
                    tcpChannel.sendMessageToServer();
                    System.out.println("t");
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
    }


}
