package manager;

import java.net.*;
import java.io.*;
import java.util.*;
import java.time.*;
import java.util.concurrent.*;

//This class is where execution starts for Manager
//It initializes threads and initiaizes their shared resources and Semeaphores to ensure mutual exclusion
public class Manager {

    public static final int PORT = 42636;

    public static void main(String args[]){
        ArrayList<Agent> agents = new ArrayList<Agent>();
        ArrayList<AgentMonitor> monitors = new ArrayList<AgentMonitor>();
        Semaphore agent_sem = new Semaphore(1);
        BeaconListener listener = new BeaconListener("listener",PORT, agents, monitors, agent_sem);
        listener.start();

    }
}

