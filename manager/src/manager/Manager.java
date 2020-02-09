package manager;

import java.net.*;
import java.io.*;

public class Manager {

    public static final int PORT = 42636;

    public static void main(String args[]){
        BeaconListener listener = new BeaconListener("listener",PORT);
        listener.start();
    }
}

