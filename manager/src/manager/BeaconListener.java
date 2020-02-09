package manager;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class BeaconListener extends Thread{
    private Thread t;
    private String threadName;
    private int port;
    private ArrayList<Agent> agents;
    private ArrayList<AgentMonitor> monitors;
    private Semaphore agent_sem;
    private Semaphore monitor_sem;

    public BeaconListener(String threadName, int port, ArrayList<Agent> agents, ArrayList<AgentMonitor> monitors, Semaphore agent_sem, Semaphore monitor_sem){
        this.threadName = threadName;
        this.port = port;
        this.agents = agents;
        this.monitors = monitors;
        this.agent_sem = agent_sem;
        this.monitor_sem = monitor_sem;
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

            System.arraycopy(data,12,bID,0,4);
            int startUpTime = byteToInt(bID);

            System.arraycopy(data,8,bID,0,4);
            char IP[] = new char[4];

            System.arraycopy(data,4,bID,0,4);
            int timeInterval = byteToInt(bID);

            System.arraycopy(data,0,bID,0,4);
            int cmdPort = byteToInt(bID);

            try {
                agent_sem.acquire();

                Agent agent = new Agent(id, startUpTime, timeInterval, IP, cmdPort);
                agents.add(agent);

                agent_sem.release();
            }
            catch (InterruptedException e){

            }
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
