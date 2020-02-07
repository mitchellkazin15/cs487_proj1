#include <iostream>

int main() {
    std::cout << "Hello, World!" << std::endl;
    return 0;
}
#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <unistd.h>
#include <sys/wait.h>
#include <dirent.h>
#include <pthread.h>
#include <stdint.h>

#include <cstdlib>
#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <sstream>
#include <queue>
#include <ctime>
#include <mutex>
#include <chrono>

struct BEACON
{
    int ID;             // randomly generated during startup
    int StartUpTime;    // the time when the client starts
    char IP[4];	    // the IP address of this client
    int timeInterval;   // beacon repeat interval
    int CmdPort; 	    // the client listens to this port for cmd
}

int main(int argc, char* argv[]){

    return 0;
}