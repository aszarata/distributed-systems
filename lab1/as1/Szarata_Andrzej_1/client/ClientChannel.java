package client;

public interface ClientChannel {

    public void run() throws InterruptedException ;

    public void createSocket();

    public void sendMessageToServer();

    public void close();

}