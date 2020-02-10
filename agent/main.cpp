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
#include <iomanip>
#include <ctime>

using namespace std;

#define UDP_PORT 42636
string *destIP;
string *srcIP;

typedef struct BEACON
{
    u_int32_t ID;               // randomly generated during startup
    u_int32_t StartUpTime;      // the time when the client starts
    u_int32_t IP;	                // the IP address of this client
    u_int32_t timeInterval;     // beacon repeat interval
    u_int32_t CmdPort; 	        // the client listens to this port for cmd
} beacon_t;

int sendBeacon(int cSock, beacon_t *buffer){
    cout << "Emitting Beacon..." << endl;
    while(true){
        size_t buffer_len = sizeof(beacon_t);

        int32_t sent_bytes = send(cSock, buffer, buffer_len, 0);
        if (sent_bytes < 0)
        {
            fprintf(stderr,"Failed to send Beacon to %s \n", destIP->c_str());
        }
        sleep(buffer->timeInterval);
    }

    return 0;
}

void * socketConnect(void *beacon){
    int cSock;
    if ((cSock = socket(AF_INET, SOCK_DGRAM, 0)) < 0){
        perror("socket");
        printf("Failed to create socket\n");
        abort ();
    }
    struct sockaddr_in sin;
    memset (&sin, 0, sizeof(sin));
    sin.sin_family = AF_INET;
    sin.sin_addr.s_addr = inet_addr(destIP->c_str());
    sin.sin_port = htons(UDP_PORT);

    if (connect(cSock, (struct sockaddr *) &sin, sizeof(sin)) < 0){
        fprintf(stderr, "Cannot connect to server\n");
        abort();
    }

    beacon_t *buffer = (beacon_t *)beacon;
    sendBeacon(cSock, buffer);
}

void * cmdAgent(void *beacon){
    beacon_t *beac = (beacon_t *)beacon;

    int sock;
    if ((sock = socket(AF_INET, SOCK_STREAM, 0)) < 0){
        perror("socket");
        printf("Failed to create socket\n");
        abort ();
    }
    struct sockaddr_in s;
    memset (&s, 0, sizeof(s));
    s.sin_family = AF_INET;
    s.sin_addr.s_addr = inet_addr(destIP->c_str());
    s.sin_port = htons(beac->CmdPort);


    if (connect(sock, (struct sockaddr *) &s, sizeof(s)) < 0){
        fprintf(stderr, "Cannot connect to server\n");
        abort();
    }

    cout << "Establishing connection on port " << s.sin_port << endl;
    char packet[1024] = "hi";
    auto t = std::time(nullptr);
    auto tm = *std::localtime(&t);
    time_t my_time = time(NULL);
    sprintf(packet, "\nInfo for agent %d\nLocal OS: Linux \nLocal Time: %s",beac->ID, ctime(&my_time));
    size_t packet_len = 1024*sizeof(char);

    write (sock, packet, strlen(packet));
    close (sock); // Close the connection
    cout << "Packet sent" << endl;
}

int main(int argc, char* argv[]){

    destIP = new string("127.0.0.1");
    srcIP = new string("127.0.0.1");
    
    if(argc == 3){
        destIP = new string(argv[1]);
        srcIP = new string(argv[2]);
    }

    srand(time(0));
    beacon_t *beacon = (beacon_t*)malloc(sizeof(beacon_t));
    beacon->ID = (u_int32_t)rand();
    beacon->StartUpTime = (u_int32_t)time(NULL);
    beacon->timeInterval = 3;
    beacon->IP = inet_addr(destIP->c_str());
    beacon->CmdPort = UDP_PORT + (rand() % 100);
    pthread_t beaconSender = *(new pthread_t);
    pthread_t cmdReciever = *(new pthread_t);
    pthread_create(&beaconSender, NULL, socketConnect, (void *)beacon);
    sleep(beacon->timeInterval);
    pthread_create(&cmdReciever, NULL, cmdAgent, (void *)beacon);
    pthread_join(cmdReciever, NULL);
    pthread_join(beaconSender, NULL);
    return 0;
}