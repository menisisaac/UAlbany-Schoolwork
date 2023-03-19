#include <semaphore.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include<sys/types.h>
#include<sys/stat.h>
#include <fcntl.h> 
#include <unistd.h>
#include <time.h>
#include <pthread.h>

pthread_mutex_t lock;
int semaCreated = 0;    //Keeps track if file and semaphore have been created
int file;               //Holds file for each process

/*
    Structure to hold function stack information + process tracking
*/
struct node {
    pid_t process_ID_Init;
    pid_t parentProcess_ID_Init;
    pid_t process_ID_Final;
    pid_t parentProcess_ID_Final;
    void* function;
    void* caller;
    void* stackFrame;
    void* startAddress;
    void* endAddress;
    int stacksize;
    struct timespec* start_time;
    struct timespec* end_time;
    long end;
    long begin;
    void* initial;
    void* final;
    struct node *next;
};
struct node* head= NULL;            
struct node* current;

/*
    Creates new node for each function enter, adds initial contents, process ids, and time
*/
void __cyg_profile_func_enter(void *this_fn, void *call_site) {
    if(!semaCreated) {
        pthread_mutex_init(&lock,NULL);
        file = open("stack.bin", O_CREAT | O_WRONLY | O_APPEND);
    }
    if(head == NULL) {
        head = malloc(sizeof(struct node));
        current = head;
    } else {
        current->next = malloc(sizeof(struct node));
        current = current->next;
        current->next = NULL;
    }
    current->start_time = (struct timespec*)malloc(sizeof(struct timespec));
    clock_gettime(CLOCK_MONOTONIC, current->start_time);
    current->begin = current->start_time->tv_nsec;    
    current->stacksize = current->startAddress - current->endAddress;    
    current->initial = malloc(sizeof(current->stacksize)*sizeof(void*));
    current->process_ID_Init = getppid();
    current->parentProcess_ID_Init = getppid();
    current->function = this_fn;
    current->caller = call_site;
    current->stackFrame = __builtin_frame_address(1);
    current->startAddress = __builtin_frame_address(1) + 2 * sizeof(void*);
    memcpy(current->initial, __builtin_frame_address(0) + 4 * sizeof(void*), current->stacksize);
}

/*
    Creates a node which keeps track of everything except initial values 
    Writes each node to file under a mutex locked write function
    50% of the time write works every time
*/
void __cyg_profile_func_exit(void *this_fn, void *call_site) {
    struct node* exit = (struct node*)malloc(sizeof(struct node));
    exit->parentProcess_ID_Final = getppid();
    exit->process_ID_Final = getpid();
    exit->process_ID_Init = getppid();
    exit->parentProcess_ID_Init = getppid();
    exit->function = this_fn;
    exit->caller = call_site;
    exit->stackFrame = __builtin_frame_address(1);
    exit->startAddress = __builtin_frame_address(1) + 2 * sizeof(void*);
    exit->endAddress = __builtin_frame_address(0) + 4 * sizeof(void*);
    exit->final = malloc(sizeof(exit->stacksize)*sizeof(void*));
    exit->initial = malloc(sizeof(exit->stacksize)*sizeof(void*));
    exit->stacksize = exit->startAddress - exit->endAddress;
    exit->end_time = (struct timespec*)malloc(sizeof(struct timespec));
    exit->start_time = (struct timespec*)malloc(sizeof(struct timespec));
    clock_gettime(CLOCK_MONOTONIC, exit->end_time);
    clock_gettime(CLOCK_MONOTONIC, exit->start_time);
    exit->end = exit->end_time->tv_nsec;
    memcpy(exit->final, exit->endAddress, exit->stacksize);
    pthread_mutex_lock(&lock);
    printf("start process: blank, start parent process: blank\nfinish process: %d, finish parent process: %d\n", exit->process_ID_Final, exit->parentProcess_ID_Final);
    printf("Function: %p Caller: %p Frame Pointer: %p\nStack Frame %p - %p Time: %ld.%.9ld\n", exit->function, exit->caller, exit->stackFrame, exit->startAddress, exit->endAddress, exit->end_time->tv_sec, exit->end_time->tv_nsec);
    for(int i = 0; i < exit->stacksize / 4; i++) {
        printf("%p-%p: %08x\n", exit->startAddress + i * 4 - 1, exit->startAddress - 4 *(i+1), *((int*)(exit->final + i * 4)));
    }
    write(file, exit, sizeof(struct node));
    write(file, exit->endAddress, exit->stacksize);
    pthread_mutex_unlock(&lock);
}