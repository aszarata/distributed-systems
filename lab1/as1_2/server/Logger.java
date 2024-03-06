package server;

public class Logger {
    
    String serverPrefix = "[SERVER] ";

    public void start(){
        System.out.println("SERVER STARTED");
    }

    public void confirm(String channelType) {
        System.out.println(serverPrefix + channelType + " working");
    }

}
