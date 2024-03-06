package client;

import java.util.Scanner;

public class InputConsole {
    
    String userPrefix = ">>";

    Scanner scanner = new Scanner(System.in);
    char input;


    public void start() {
        
        do {
            System.out.print(">>");
            input = scanner.next().charAt(0);

            switch (input) {
                // TCP communication
                case 't':
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
