#include "services/application/tagParser.c"
#include "services/application/tagCreator.c"
#include <stdio.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <pthread.h>
#include <stdlib.h>

int sock;


void printMessage(char* username, char* enc) {
    char temp[18];
    strcpy(temp, "From: ");
    strcat(temp, username);
    strcat(temp, " - ");
    write(0, temp, strlen(temp) + 1);
    char buff;
    consumeMsgTags(sock);
    while(read(sock, &buff, 1) == 1) {
        if(buff == '<') {
            consumeMsgTags(sock);
            consumerTagEnd(sock);
            break;
        }
        write(0, &buff, 1);
    }    
}

void printResponse() {
    char* serverMessage = "Server Info: ";
    write(0, serverMessage, strlen(serverMessage) + 1);
    char buff;
    while(read(sock, &buff, 1) == 1) {
        if(buff == '<') {
            consumerTagEnd(sock);
            break;
        }
        write(0, &buff, 1);
    }
}

void processMessage() {
    int conn = sock;
    char username[9];
    char to[9];
    char userlist[49];
    char enc[4];
    int type = -1;

    char first;
    int bytes = read(conn, &first, 1);
    if (bytes == 0) {
        exit(0);
        return;
    } 
    char* message = parse(first, conn, username, to, userlist, enc, &type);

    switch(type) {
        case -1:
            break;
        case 2: 
            printMessage(username, enc);
            break;
        case 4:
            printResponse();
            break;
    }
}

void *recieve() {
    char buff;
    char newline = '\n';
    while(1) {
        processMessage();
        char ending = '\n';
        write(0, &ending, 1);
    }
    exit(0);
}





int main(int argc, char** argv) {
    pthread_t reciever;
    if (argc == 4) {
        fprintf(stderr, "Must provide server ip & port");
    }
    struct sockaddr_in servaddr;
    servaddr.sin_family = AF_INET;
    servaddr.sin_addr.s_addr = inet_addr(argv[1]);
    servaddr.sin_port = htons(atoi(argv[2]));
    sock = socket(AF_INET, SOCK_STREAM, 0);
    connect(sock, (struct sockaddr*)&servaddr, sizeof(servaddr));
    int result = pthread_create(&reciever, NULL, recieve, NULL);
    char user[9];
    printf("Enter a 8 character username: ");
    scanf("%s", user);

    char login[loginSize(user)];
    createLogin(login, user);
    write(sock, login, strlen(login) + 1);

    char loginList[userlistSize()];
    createUserlist(loginList);
    write(sock, loginList, strlen(loginList) + 1);

    char logout[logoutSize(user)];
    createLogout(logout, user);
    write(sock, logout, strlen(logout) + 1);


    char userInput[4096];
    char quit[] = "quit";
    while(strcmp(userInput, quit) != 0) {
        scanf("%s", userInput);
    }

}