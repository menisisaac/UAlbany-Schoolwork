#include "services/application/tagParser.c"
#include "services/application/tagCreator.c"
#include <stdio.h>
#include <sys/socket.h>
#include <unistd.h>
#include <netinet/in.h>
#include <sys/time.h>
#include <fcntl.h>

#define PORT 10000
#define CONNECTIONLIMIT 6

int CONNECTIONCOUNT = 0;
char connections[6][9];
int clients[6];


void init() {
    for(int i = 0; i < CONNECTIONLIMIT; i++) {
        connections[i][0] = -1;
        connections[i][8] = 0;
        clients[i] = 0;
    }
}


FILE* getFile(char* name) {
    char filename[strlen(name) + 5];
    strcpy(filename, name);
    strcat(filename, ".txt");
    return fopen(filename, "a");
}






void closeSock(int conn) {
    for(int i = 0; i < CONNECTIONLIMIT; i++) {
        if(clients[i] == conn) {
            connections[i][0] = -1;
            clients[i] = 0;
        }
    }
    close(conn);

}
void loginList(int conn) {
    char users[9 * CONNECTIONCOUNT];
    int added = 0;
    for(int i = 0; i < CONNECTIONLIMIT; i++) {
        if(connections[i][0] != -1) {
            if (added++ == 0) {
                strcpy(users, connections[i]);
                if(i != CONNECTIONCOUNT - 1) {
                    int length = strlen(users);
                    users[length] = ',';
                    users[length + 1] = 0;
                } 
            } else {
                strcat(users, connections[i]);
                if(i != CONNECTIONCOUNT - 1) {
                    int length = strlen(users);
                    users[length] = ',';
                    users[length + 1] = 0;
                } else {
                    int length = strlen(users);
                    users[length] = 0;
                }
            }
        }
    }
    char message[responseSize(users)];
    createResponse(message, users);
    write(conn, message, strlen(message) + 1);
}

void login(int conn, char* username) {
    int idk = -1;
    if (CONNECTIONCOUNT < CONNECTIONLIMIT) {
        for(int i = 0; i < CONNECTIONLIMIT; i++) {
            if (clients[i] == conn) {
                idk = i;
                strcpy(connections[i], username);
                connections[i][8] = 0;
                break;
            }
        }
        CONNECTIONCOUNT++;
        char* loginInfo = "Welcome to 500-chat";
        char message[responseSize(loginInfo)];
        createResponse(message, loginInfo);
        write(conn, message, strlen(message) + 1);
    }
}

void removeFiles(char * username) {

}

void logout(int conn, char* username) {
    char* logoutMessage = "Successfully Logged Out";
    char logoutMess[responseSize(logoutMessage)];
    createResponse(logoutMess, logoutMessage);
    write(conn, logoutMess, strlen(logoutMess) + 1);
    removeFiles(username);
}

void forwardMessage(int conn, char* enc, char* from, char* to) {
    int toExists = 0;
    FILE* output;
    for(int i = 0; i < CONNECTIONLIMIT; i++) {
        if(connections[i][0] != -1 & strcmp(to, connections[i]) == 0) {
            toExists = clients[i];
            char name[strlen(from) + strlen(to) + 1];
            strcpy(name, from);
            strcat(name, to);
            output = getFile(name);
            break;
        }
    }
    fprintf(stderr, "Exists: %d", toExists);
    if(toExists == 0) {
        char* noUserFound = "Message Could Not Be Sent because the User is not on this server!";
        char noUser[responseSize(noUserFound)];
        createResponse(noUser, noUserFound);
        write(conn, noUser, strlen(noUser) + 1);
    }

    if(toExists > 0) {
        char temp[startMessageSize(from, to)];
        createStartMessage(temp, enc, from, to);
        write(toExists, temp, strlen(temp));
        fputs(temp, output);
    }
    char buff; 
    char* endTag = "</BODY>";
    int endIndex = 0;
    while(read(conn, &buff, 1) == 1) {
        if(buff == endTag[endIndex]) {
            endIndex++;
            if(endIndex == 7) {
                break;
            }
        } else {
            endIndex = 0;
        }
        if(toExists > 0) {
            write(toExists, &buff, 1);
            fputc(buff, output);
        }
    }
    if(toExists > 0) {
        write(toExists, &buff, 1);
        fputc(buff, output);
    }

    char* endingMessage = "</MSG>";
    if(toExists > 0) {
        write(toExists, endingMessage, strlen(endingMessage) + 1);
        fputs(endingMessage, output);
    }
    consumerTagEnd(conn);
    fputc('\n', output);
    fclose(output);
}


void processMessage(int conn) {
    char username[9];
    char to[9];
    char userlist[49];
    char enc[4];
    int type = -1;
    /*
    Check if message is empty
        If so, close socket
    Read in tag and parse 
    If admin stuff, issue appropriate response
    If message tag, read in message to file and write to appriopriate client sockets
    */
    char first;
    int bytes = read(conn, &first, 1);
    if (bytes == 0) {
        closeSock(conn);
        //Logout if sock had user
        return;
    } 
    char* message = parse(first, conn, username, to, userlist, enc, &type);
    switch(type) {
        case -1:
            break;
        case 0:
            login(conn, username);
            break;
        case 1:
            logout(conn, username);
            break;
        case 2: 
            forwardMessage(conn, enc, username, to);
            break;
        case 3:
            loginList(conn);
            break;
    }
}


int main() {
    init();
    int sock, conn, max, selected;
    fd_set fds;
    struct sockaddr_in servaddr, client;
    sock = socket(AF_INET, SOCK_STREAM, 0);
    servaddr.sin_family = AF_INET;
    servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
    servaddr.sin_port = htons(PORT);
    bind(sock, (struct sockaddr*)&servaddr, sizeof(servaddr));
    listen(sock, 5);
    int length = sizeof(client);
    while(1) {
        FD_ZERO(&fds);
        FD_SET(sock, &fds);
        max = sock;
        for(int i = 0; i < CONNECTIONLIMIT; i++) {
            conn = clients[i];
            if(conn > 0) {
                FD_SET(conn, &fds);
            }
            if(conn > max) {
                max = conn;
            }
        }
        selected = select(max + 1, &fds, NULL, NULL, NULL);
        if (FD_ISSET(sock, &fds)) {
            if(CONNECTIONCOUNT < 6) {
                conn = accept(sock, (struct sockaddr*)&client, &length);
                for(int i = 0; i < CONNECTIONLIMIT; i++) {
                    if(clients[i] == 0) {
                        clients[i] = conn;
                        break;
                    }
                }
            } else {
                close(accept(sock, (struct sockaddr*)&client, &length));
            }
        } else {
            for(int i = 0; i < CONNECTIONLIMIT; i++) {
                conn = clients[i];
                if(FD_ISSET(conn, &fds)) {
                    processMessage(conn);

                    break;
                }
            }
        }
    }
    return 0;
}