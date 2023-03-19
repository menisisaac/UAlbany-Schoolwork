#include <signal.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>

int pipeOne[2];
int pipeTwo[2];

//Exit process, includes message
void sigusr1_handler(int sig) {
    (void)sig;
    printf("Pong quitting");
    exit(0);
}
//Starts the back and forth count, used by child 1
void ping(void) {
    int i = 0;
    while(i < 100) {
        printf("ping: %i\n",i);
        i += 1;
        write(pipeOne[1], &i, sizeof(i));
        read(pipeTwo[0], &i, sizeof(i));
    }
    exit(0);
}
//Responds to child one creating ping pong count, assigns signal handler to signal 
void pong(void) {
    signal(SIGUSR1, sigusr1_handler);
    int i;
    while(1) {
        read(pipeOne[0], &i, sizeof(i));
        printf("pong: %i\n",i);
        i += 1;
        write(pipeTwo[1], &i, sizeof(i));
    }
}

//Creates 2 pipes, two children, and assigns each child a function
int main(void) {
    pipe(pipeOne);
    pipe(pipeTwo);
    pid_t childOne = fork();
    if(childOne == 0) {
        ping();
    } else {
        pid_t childTwo = fork();
        if(childTwo == 0) {
            pong();
        } else {
            wait(childOne);
            kill(childTwo, SIGUSR1);
        }
    }
    return 0;
}