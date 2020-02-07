import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketException;
import java.io.IOException;

public class Manager {

    private DatagramSocket socket;

    public static void main(String args[]){
        System.out.println("hi");
    }

    private class BeaconListener extends Thread {

        @Override
        public void run() {
            byte buffer[] = new byte[1024];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
            DatagramSocket ds = null;
            try {
                ds = new DatagramSocket(3982);

            }
            catch(SocketException e){

            }

            while(true){
                try {
                    ds.receive(incoming);
                }
                catch (IOException e){

                }
                byte[] data = new byte[incoming.getLength()];
                System.arraycopy(incoming.getData(), 0, data, 0, data.length);
                new DataProcessor(data).start();
            }
        }
    }

    private class DataProcessor extends Thread {
        byte data[];

        private DataProcessor(byte data[]){
            this.data = data;
        }
    }

    private class AgentManager extends Thread {

    }
}
