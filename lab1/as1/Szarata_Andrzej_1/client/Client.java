package client;

public class Client {

    int portNumber;
    String hostName;

    int multicastPortNumber;
    String MULTICAST_ADDRESS;

    public Client(int portNumber, String hostName, int multicastPortNumber, String multicastAddress){
        this.portNumber = portNumber;
        this.hostName = hostName;
        this.multicastPortNumber = multicastPortNumber;
        MULTICAST_ADDRESS = multicastAddress;
    }

    public void run() throws InterruptedException {

        ClientTcpChannel tcpChannel = new ClientTcpChannel(portNumber, hostName);
        tcpChannel.createSocket();

        ClientUdpChannel udpChannel = new ClientUdpChannel(portNumber, tcpChannel.getLocalPort(), multicastPortNumber, MULTICAST_ADDRESS);     
        udpChannel.createSocket();
        
        InputConsole inputConsole = new InputConsole(tcpChannel, udpChannel);
        Thread console = new Thread(inputConsole);

        console.start();

        // start network channels
        tcpChannel.run();        
        udpChannel.run();

        console.join();

        // close nertwork channels
        tcpChannel.close();
        udpChannel.close();

    }

}
