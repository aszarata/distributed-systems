package client;

public class Client {

    int portNumber;
    String hostName;

    InputConsole console = new InputConsole();

    public Client(int portNumber, String hostName){
        this.portNumber = portNumber;
        this.hostName = hostName;
    }

    public void run() throws InterruptedException {

        ClientTcpChannel tcpChannel = new ClientTcpChannel(portNumber, hostName);

        Thread tcp = new Thread(tcpChannel);

        tcp.start();

    }


}
