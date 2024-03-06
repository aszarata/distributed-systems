import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class JavaUdpServer {

    public static void main(String args[]) {
        System.out.println("JAVA UDP SERVER");
        DatagramSocket socket = null;
        int portNumber = 9008;

        try {
            socket = new DatagramSocket(portNumber);
            byte[] receiveBuffer = new byte[1024];

            while (true) {
                Arrays.fill(receiveBuffer, (byte) 0);

                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);

                int nb = ByteBuffer.wrap(receiveBuffer).getInt();
                
                System.out.println("recieved msg: " + nb);
                

                // potwierdzenie
                byte[] confirmationBuffer = ByteBuffer.allocate(4).putInt(nb+1).array();
                // eceiveBuffer.allocate(4).putInt(nb).array();
                DatagramPacket confirmationPacket = new DatagramPacket(confirmationBuffer, confirmationBuffer.length,
                            receivePacket.getAddress(), receivePacket.getPort());
                    socket.send(confirmationPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}
