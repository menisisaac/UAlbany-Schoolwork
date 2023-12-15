#include <string.h>
#include <unistd.h>
#include <stdio.h>


/*
Username Max = 8 Character, starting with letter
*/

void consumerTagEnd(int conn) {
    char buff;
    while(read(conn, &buff, 1) == 1) {
        if (buff == '>') {
            break;
        }
    }
    read(conn, &buff, 1);
}
void consumeMsgTags(int conn) {
    char buff;
    while(read(conn, &buff, 1) == 1) {
        if (buff == '>') {
            break;
        }
    }
}

void parseUser(int conn, char* user) {
    int index = 0;
    while(read(conn, user + index, 1) == 1) {
        if (user[index] == '<') {
            user[index] = 0;
            break;
        }
        index++;
    }
}
void parseEnc(int conn, char* enc) {
    int index = 0;
    while(read(conn, enc + index, 1) == 1) {
        if (enc[index] == '<') {
            enc[index] = 0;
            break;
        }
        index++;
    }
}

void parseMessage(int conn, char* user, char* to, char* enc) {
    consumeMsgTags(conn);
    parseEnc(conn, enc);
    consumeMsgTags(conn);
    consumeMsgTags(conn);
    parseUser(conn, user);
    consumeMsgTags(conn);
    consumeMsgTags(conn);    
    parseUser(conn, to);
    consumeMsgTags(conn);
}


char* parse(char first, int conn, char* user, char* to, char* userlist, char* enc, int* type) {
    char tag[15];
    tag[0] = first;
    int index = 1;
    while(read(conn, tag + index, 1) != 0 && index < 15) {
        if (tag[index] == '>') {
            index++;
            tag[index] = 0;
            break;
        }
        index++;
        if (index == 15) {
            tag[index] = 0;
        }
    }
    if(strcmp(tag, "<LOGIN>") == 0) {
        *type = 0;
        parseUser(conn, user);
        consumerTagEnd(conn);
    } else if(strcmp(tag, "<LOGOUT>") == 0) {
        *type = 1;
        parseUser(conn, user);
        consumerTagEnd(conn);
    } else if(strcmp(tag, "<MSG>") == 0) {
        *type = 2;
        parseMessage(conn, user, to, enc);
    } else if(strcmp(tag, "<LOGIN_LIST>") == 0) {
        *type = 3;
        consumerTagEnd(conn);
    } else if(strcmp(tag, "<INFO>") == 0) {
        *type = 4;
    } else {
        *type = -1;
    }
    return NULL;











    
}