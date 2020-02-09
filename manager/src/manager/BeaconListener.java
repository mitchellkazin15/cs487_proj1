package manager;

import java.net.*;
import java.io.*;

public class BeaconListener extends Thread{
    private Thread t;
    private String threadName;
    private int port;

    public BeaconListener(String threadName, int port){
        this.threadName = threadName;
        this.port = port;
    }

    @Override
    public void start(){
        System.out.println("Starting " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }

    @Override
    public void run(){
        System.out.println("Running " +  threadName );
        byte buffer[] = new byte[1024];
        DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(port);

        }
        catch(SocketException e){
            System.out.println("Could not open Socket on port: " + this.port);
            System.out.println(e.getMessage());
            return;
        }

        while(true){
            System.out.println("Listening for beacon on port " + ds.getLocalPort() + "...");
            try {
                ds.receive(incoming);
            }
            catch (IOException e){
                continue;
            }
            byte[] data = new byte[incoming.getLength()];
            System.arraycopy(incoming.getData(), 0, data, 0, data.length);
            DataProcessor dp = new DataProcessor("dp", data);
            dp.start();
        }
    }

    private class DataProcessor extends Thread {
        private Thread t;
        private String threadName;
        private byte[] data;

        public DataProcessor(String threadName, byte[] data){
            this.threadName = threadName;
            this.data = data;
            reverse(this.data);
        }

        @Override
        public void run(){
            if(data.length != 20){
                System.out.println(data.length);
                return;
            }
            byte[] bID = new byte[4];
            System.arraycopy(data,16,bID,0,4);
            int id = byteToInt(bID);
            System.out.println("ID is : " + id);
            System.arraycopy(data,12,bID,0,4);
            int startUpTime = byteToInt(bID);
            System.out.println("Start time is : " + startUpTime);
            System.arraycopy(data,4,bID,0,4);
            int timeInterval = byteToInt(bID);
            System.out.println("Time interval is : " + timeInterval);
            System.arraycopy(data,0,bID,0,4);
            int cmdPort = byteToInt(bID);
            System.out.println("Command Port is : " + cmdPort);
        }

        @Override
        public void start(){
            if (t == null) {
                t = new Thread (this, threadName);
                t.start ();
            }
        }

        private int byteToInt(byte[] bytes){
            return   bytes[3] & 0xFF |
                    (bytes[2] & 0xFF) << 8 |
                    (bytes[1] & 0xFF) << 16 |
                    (bytes[0] & 0xFF) << 24;
        }


        private void reverse(byte[] array) {
            if (array == null) {
                return;
            }
            int i = 0;
            int j = array.length - 1;
            byte tmp;
            while (j > i) {
                tmp = array[j];
                array[j] = array[i];
                array[i] = tmp;
                j--;
                i++;
            }
        }


    }
}
