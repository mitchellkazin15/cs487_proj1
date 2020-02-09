package manager;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Manager {

    public static final int PORT = 42636;

    public static void main(String args[]){
        ArrayList<Agent> agents = new ArrayList<Agent>();
        ArrayList<AgentMonitor> monitors = new ArrayList<AgentMonitor>();
        Semaphore agent_sem = new Semaphore(1);
        Semaphore monitor_sem = new Semaphore(1);
        BeaconListener listener = new BeaconListener("listener",PORT, agents, monitors, agent_sem, monitor_sem);
        listener.start();

    }
}

