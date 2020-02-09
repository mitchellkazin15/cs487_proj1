#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <unistd.h>
#include <sys/wait.h>
#include <dirent.h>
#include <pthread.h>
#include <stdint.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <memory.h>
#include <ifaddrs.h>
#include <net/if.h>

#include <cstdlib>
#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <sstream>
#include <queue>
#include <ctime>
#include <thread>
#include <mutex>
#include <chrono>

using namespace std;

#define UDP_PORT 42636

typedef struct BEACON
{
    u_int32_t ID;               // randomly generated during startup
    u_int32_t StartUpTime;      // the time when the client starts
    char IP[4];	                // the IP address of this client
    u_int32_t timeInterval;     // beacon repeat interval
    u_int32_t CmdPort; 	        // the client listens to this port for cmd
} beacon_t;

int sendBeacon(int cSock, beacon_t *buffer){
    while(true){
        size_t buffer_len = sizeof(beacon_t);

        int32_t sent_bytes = send(cSock, buffer, buffer_len, 0);
        if (sent_bytes < 0)
        {
            fprintf(stderr,"cannot send. \n");
        }
        cout << "Beacon Sent" << endl;
        sleep(buffer->timeInterval);
    }

    return 0;
}

void socketConnect(beacon_t *beacon){
    int cSock;
    if ((cSock = socket(AF_INET, SOCK_DGRAM, 0)) < 0){
        perror("socket");
        printf("Failed to create socket\n");
        abort ();
    }
    struct sockaddr_in sin;
    memset (&sin, 0, sizeof(sin));
    sin.sin_family = AF_INET;
    sin.sin_addr.s_addr = inet_addr("127.0.0.1");
    sin.sin_port = htons(UDP_PORT);

    if (connect(cSock, (struct sockaddr *) &sin, sizeof(sin)) < 0){
        fprintf(stderr, "Cannot connect to server\n");
        abort();
    }

    sendBeacon(cSock, beacon);
}

int main(int argc, char* argv[]){
    srand(time(0));
    beacon_t *beacon = (beacon_t*)malloc(sizeof(beacon_t));
    beacon->ID = (u_int32_t)rand();
    beacon->StartUpTime = (u_int32_t)time(NULL);
    beacon->timeInterval = 3;
    beacon->CmdPort = UDP_PORT + (rand() % 100);
    socketConnect(beacon);
    return 0;
}