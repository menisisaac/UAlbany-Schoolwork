#include <stdio.h>
#include <pthread.h>
#include <semaphore.h>
#include <string.h>
#include <stdio.h>
#include <ctype.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <unistd.h>
#define SA struct sockaddr

struct circularQueue {
    sem_t lock;
    sem_t fullSlots;
    sem_t emptySlots;
    int start;
    int end;
    char letters[5];
};
typedef struct circularQueue circularQueue;
void push(circularQueue* cq, char letter) {
    sem_wait(&cq->emptySlots);
    sem_wait(&cq->lock);
    cq->letters[(cq->end + 1) % 5] = letter;
    cq->end = (cq->end + 1) % 5;
    sem_post(&cq->lock);
    sem_post(&cq->fullSlots);
}
char pop(circularQueue* cq) {
    sem_wait(&cq->fullSlots);
    sem_wait(&cq->lock);
    char letter = cq->letters[cq->start];
    cq->start = (cq->start + 1) % 5;
    sem_post(&cq->lock);
    sem_post(&cq->emptySlots);
    return letter;
}


int init_circularQueue(circularQueue* cq) {
    cq->start = 0;
    cq->end = -1;
    sem_init(&(cq->emptySlots), 0, 5);
    sem_init(&(cq->fullSlots), 0, 0);
    sem_init(&(cq->lock), 0, 1);
}


char assignment[] = {'a', 'e', 'i', 'o', 'u'};
circularQueue tasks[6];
long input = 0;
long output = 0;

void init() {
    for(int i = 0; i < 6; i++) {
        init_circularQueue(tasks + i);
    }
}

void *lowercaseToUppercase(void* assignmentNumber) {
    int aN = *((int*)assignmentNumber);
    char replace = assignment[aN];
    circularQueue* previous = &tasks[aN];
    circularQueue* next = &tasks[aN + 1];
    while(1) {
        char letter = pop(previous);
        if(letter == replace) {
            letter = toupper(letter);
        }
        push(next, letter);
    }
}
void *writer(void* connection) {
    int conn = *(int*)connection;
    circularQueue* previous = &tasks[5];
    while(1) {
        char letter = pop(previous);
        write(conn, &letter, 1);
        output++;
    }
}

void serverDecoder(int conn) {
    pthread_t helpers[6];
    int iret[6];
    int value[] = {0, 1, 2, 3, 4, 5};
    char ch;
    init();
    for(int i = 0; i < 5; i++) {
        iret[i] = pthread_create(helpers + i, NULL, lowercaseToUppercase, (void*)(value + i));
    }
    iret[5] = pthread_create(helpers + 5, NULL, writer, (void*)&conn);
    char* message = "hallo my name is isaac and i live under a bridge";
    while(read(conn, &ch, 1) != 0) {
        if(ch == 0) {
            break;
        }
        input++;
        push(&tasks[0], ch);
    }
    while(1) {
        if(input == output) {
            for(int i = 0; i < 6; i++) {
                pthread_cancel(helpers[i]);
            }
            break;
        }
    }
}



int main() {
    int sockfd, connfd;
    struct sockaddr_in servaddr, cli;
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    servaddr.sin_family = AF_INET;
    servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
    servaddr.sin_port = htons(4000);
    bind(sockfd, (struct sockaddr*)&servaddr, sizeof(servaddr));
    listen(sockfd, 5);
    int length = sizeof(cli);
    while(1) {
        connfd = accept(sockfd, (struct sockaddr*)&cli, &length);
        printf("COnnected");
        fflush(stdout);
        serverDecoder(connfd);
    }

}