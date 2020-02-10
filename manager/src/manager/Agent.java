package manager;
import java.net.*;
import java.io.*;
import java.util.*;
import java.time.*;
import java.util.concurrent.*;

public class Agent {
    public int id;
    public int startUpTime;
    public int timeInterval;
    public InetAddress IP;
    public int cmdPort;
    public long lastBeacon;

    public Agent(int id, int startUpTime, int timeInterval, InetAddress IP, int cmdPort){
        this.id = id;
        this.startUpTime = startUpTime;
        this.timeInterval = timeInterval;
        this.IP = IP;
        this.cmdPort = cmdPort;
        this.lastBeacon = Instant.now().getEpochSecond();
    }

    public void printAgent(){
        System.out.println("Agent ID is: " + id);
        System.out.println("Start time is: " + startUpTime);
        System.out.println("IP Address is: " + IP);
        System.out.println("Time interval is: " + timeInterval);
        System.out.println("Command Port is: " + cmdPort);
    }

    @Override
    public boolean equals(Object o){
        if (o == this) {
            return true;
        }
        if (!(o instanceof Agent)) {
            return false;
        }

        Agent a = (Agent)o;

        if(this.id == a.id){
            if(this.startUpTime == a.startUpTime){
                return true;
            }
        }
        return false;
    }
}
