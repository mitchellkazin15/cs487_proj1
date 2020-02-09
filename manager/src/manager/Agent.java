package manager;

public class Agent {
    public int id;
    public int startUpTime;
    public int timeInterval;
    public char IP[];
    public int cmdPort;

    public Agent(int id, int startUpTime, int timeInterval, char IP[], int cmdPort){
        this.id = id;
        this.startUpTime = startUpTime;
        this.timeInterval = timeInterval;
        this.IP = IP;
        this.cmdPort = cmdPort;
    }

    public void printAgent(){

        System.out.println("ID is : " + id);
        System.out.println("Start time is : " + startUpTime);
        System.out.println("IP Address is : " + IP);
        System.out.println("Time interval is : " + timeInterval);
        System.out.println("Command Port is : " + cmdPort);
    }
}
