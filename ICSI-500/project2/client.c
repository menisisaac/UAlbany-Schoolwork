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
    servaddr.sin_addr.s_addr = inet_addr("127.0.0.1");
    servaddr.sin_port = htons(10000);
    sock = socket(AF_INET, SOCK_STREAM, 0);
    connect(sock, (struct sockaddr*)&servaddr, sizeof(servaddr));
    int result = pthread_create(&reciever, NULL, recieve, NULL);
    char user[9];
    printf("What is your name: ");
    scanf("%s", user);
    char log[loginSize(user)];
    createLogin(log, user);
    write(sock, log, strlen(log) + 1);

    char userList[userlistSize()];
    createUserlist(userList);

    char logout[logoutSize(user)];
    createLogout(logout, user);


    


    char userInput[4096];
    char quit[] = "quit";
    while(strcmp(userInput, quit) != 0) {
        scanf("%s", userInput);

        if(strcmp(userInput, "userlist") == 0) {
            write(sock, userList, strlen(userList) + 1);
        } else {
            char to[9];
            char enc[4];
            printf("\nEnter 8 letter username: ");
            scanf("%s", to);
            printf("\nInput encoding method: ");
            scanf("%s", enc);

            if(strstr(userInput, ".txt") != NULL) {
                FILE* fp;
                if((fp = fopen(userInput, "r")) != NULL) {
                    char messageBegin[startMessageSize(user, to)];
                    createStartMessage(messageBegin, enc, user, to);
                    write(sock, messageBegin, strlen(messageBegin));
                    char* body = "<BODY>";
                    write(sock, body, strlen(body));
                    char ch;
                    while((ch = fgetc(fp)) != EOF) {
                        write(sock, &ch, 1);
                    }
                    char* endingMessage = "</BODY></MSG>";
                    write(sock, endingMessage, strlen(endingMessage) + 1);
                    userInput[0] = 0;
                } else {
                    fprintf(stderr, "File doesn't exist");
                }
            } else {
                char temp[messageSize(user, to, userInput)];
                createMessage(temp, enc, user, to, userInput);        
                write(sock, temp, strlen(temp) + 1);
            }
        }
    }
    write(sock, logout, strlen(logout) + 1);
    close(sock);
    exit(0);
    return 0;
}