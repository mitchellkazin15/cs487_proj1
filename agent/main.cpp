#include <stdio.h>
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

#define PORT 42636

typedef struct BEACON
{
    u_int32_t ID;               // randomly generated during startup
    u_int32_t StartUpTime;      // the time when the client starts
    char IP[4];	                // the IP address of this client
    u_int32_t timeInterval;     // beacon repeat interval
    u_int32_t CmdPort; 	        // the client listens to this port for cmd
} beacon_t;

int sendBeacon(int cSock){

        beacon_t *buffer = (beacon_t*)malloc(sizeof(beacon_t));
        buffer->ID = (u_int32_t)rand();
        buffer->StartUpTime = (u_int32_t)time(NULL);
        buffer->timeInterval = 60;
        buffer->CmdPort = PORT;
        size_t buffer_len = sizeof(beacon_t);

        cout << "Sending beacon..." << endl;
        int32_t sent_bytes = send(cSock, buffer, buffer_len, 0);
        if (sent_bytes < 0)
        {
            fprintf(stderr,"cannot send. \n");
        }
        return 0;

}

void socketConnect(){
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
    sin.sin_port = htons(PORT);

    if (connect(cSock, (struct sockaddr *) &sin, sizeof(sin)) < 0){
        fprintf(stderr, "Cannot connect to server\n");
        abort();
    }

    sendBeacon(cSock);
}

int main(int argc, char* argv[]){
    socketConnect();
    return 0;
}