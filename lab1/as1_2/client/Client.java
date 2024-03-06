package client;

public class Client {

    int portNumber;
    String hostName;

    public Client(int portNumber, String hostName){
        this.portNumber = portNumber;
        this.hostName = hostName;
    }

    public void run() throws InterruptedException {

        ClientTcpChannel tcpChannel = new ClientTcpChannel(portNumber, hostName);
        tcpChannel.createSocket();
        
        InputConsole inputConsole = new InputConsole(tcpChannel);
        Thread console = new Thread(inputConsole);

        console.start();

        tcpChannel.run();        

        console.join();


    }


}
