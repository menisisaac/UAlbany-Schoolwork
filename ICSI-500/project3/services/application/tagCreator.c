#include <string.h>
#include <stdio.h>

int loginSize(char* username) {
    return strlen(username) + 16;
}
void createLogin(char* message, char* username) {
    strcpy(message, "<LOGIN>");
    strcat(message, username);
    strcat(message, "</LOGIN>");
    message[strlen(message)] = 0;
}
int logoutSize(char* username) {
    return strlen(username) + 18;
}
void createLogout(char* message, char* username) {
    strcpy(message, "<LOGOUT>");
    strcat(message + strlen(message), username);
    strcat(message + strlen(message), "</LOGOUT>");
    message[strlen(message)] = 0;    
}
int responseSize(char* info) {
    return strlen(info) + 14;
}
void createResponse(char* message, char* info) {
    strcpy(message, "<INFO>");
    strcat(message + strlen(message), info);
    strcat(message + strlen(message), "</INFO>");
    message[strlen(message)] = 0;    
}

void createEncoding(char* message, char* enc) {
    strcpy(message, "<ENCODE>");
    strcat(message + strlen(message), enc);
    strcat(message + strlen(message), "</ENCODE>");
    message[strlen(message)] = 0;
}

int userlistSize() {
    return 26;
}

void createUserlist(char* message) {
    char* tag = "<LOGIN_LIST></LOGIN_LIST>";
    strcpy(message, tag);
    message[strlen(message)] = 0;
}



int messageSize(char* from, char* to, char* message) {
    return 67 + strlen(from) + strlen(to) + strlen(message);
}

int startMessageSize(char* from, char* to) {
    return 67 + strlen(from) + strlen(to);
}

void createStartMessage(char* buffer, char* enc, char* from, char* to) {
    strcpy(buffer, "<MSG>");
    strcat(buffer, "<ENCODE>");
    strcat(buffer, enc);
    strcat(buffer, "</ENCODE>");
    strcat(buffer, "<FROM>");
    strcat(buffer, from);
    strcat(buffer, "</FROM>");
    strcat(buffer, "<TO>");
    strcat(buffer, to);
    strcat(buffer, "</TO>");
    buffer[strlen(buffer)] = 0;
}


void createMessage(char* buffer, char* enc, char* from, char* to, char* message) {
    strcpy(buffer, "<MSG>");
    strcat(buffer, "<ENCODE>");
    strcat(buffer, enc);
    strcat(buffer, "</ENCODE>");
    strcat(buffer, "<FROM>");
    strcat(buffer, from);
    strcat(buffer, "</FROM>");
    strcat(buffer, "<TO>");
    strcat(buffer, to);
    strcat(buffer, "</TO>");
    strcat(buffer, "<BODY>");
    strcat(buffer, message);
    strcat(buffer, "</BODY>");
    strcat(buffer, "</MSG>");
    buffer[strlen(buffer)] = 0;
}