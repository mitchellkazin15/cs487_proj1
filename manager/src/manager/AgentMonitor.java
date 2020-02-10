package manager;

import java.net.*;
import java.io.*;
import java.util.*;
import java.time.*;
import java.util.concurrent.*;

public class AgentMonitor extends Thread {
    private Thread t;
    private String threadName;
    private int port;
    private ArrayList<Agent> agents;
    private Semaphore agent_sem;
    private Agent agent;

    public AgentMonitor(String threadName, int port, Agent agent, ArrayList<Agent> agents, Semaphore agent_sem){
        this.agent = agent;
        this.threadName = threadName;
        this.port = port;
        this.agents = agents;
        this.agent_sem = agent_sem;
    }

    @Override
    public void start(){
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }

    @Override
    public void run(){
        try {
            ServerSocket ss = new ServerSocket(agent.cmdPort, 0, InetAddress.getByName(null));
            System.out.println("Waiting for agent " + agent.id + " to execute commands on port: " + ss.getLocalPort() + "...");
            Socket s = ss.accept();
            String buffer;
            BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintStream pstream = new PrintStream(s.getOutputStream());
            pstream.println("GET /");
            while ((buffer = reader.readLine()) != null) {
                System.out.println(buffer);
            }
        }
        catch (IOException e){
            System.out.println("Could not connect to tcp socket: " + agent.cmdPort);
        }
        try {
            while (true) {
                Thread.sleep(agent.timeInterval * 1000);

                agent_sem.acquire();

                if( Instant.now().getEpochSecond() - agent.lastBeacon > 2 * agent.timeInterval){
                    System.out.println();
                    System.out.println("Agent " + agent.id + " has died");
                    agent_sem.release();
                    break;
                }

                agent_sem.release();
            }

        }
        catch (InterruptedException e){

        }
    }

}
