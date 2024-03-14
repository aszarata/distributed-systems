package server;

public class Logger {
    
    String serverPrefix = "[SERVER] ";
    
    // Display
    private String clientPrefix(int clientID) {
        return "[CLIENT " + clientID + "] ";
    }

    public void start(){
        System.out.println("SERVER STARTED");
    }

    public void confirm(String channelType) {
        System.out.println(serverPrefix + channelType + " working");
    }

    public void logClient(int index, String address) {
        System.out.println(serverPrefix + address + " logged in as CLIENT " + index);
    }

    public void displayMssage(String message, int senderID) {
        System.out.println(clientPrefix(senderID) + message);
    }

    public void displayLogout( int senderID ) {
        System.out.println(clientPrefix(senderID) + "socket closed");
    }

    // send message
    public String tcpReply() {
        return serverPrefix + "Pong Java TCP";
    }

    public String directedMessage( String message, int senderID ){
        return "[CLIENT " + senderID + "] " + message;
    }

    public String udpReply() {
        return serverPrefix + "Pong Java UDP";
    }

}
