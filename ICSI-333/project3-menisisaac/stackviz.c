#include<sys/types.h>
#include<sys/stat.h>
#include <fcntl.h> 
#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <time.h>
#include <string.h>

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

int counter = 0;        //Counter for printing function stacks

/*
    Given the head of linked list, prints out stack information
    @param next Sorted linked list
*/
void print(struct node* next) {
    printf("(%i)\nstart process: blank, start parent process: blank\nfinish process: %d, finish parent process: %d\n", counter, next->process_ID_Final, next->parentProcess_ID_Final);
    printf("Function: %p Caller: %p Frame Pointer: %p\nStack Frame %p - %p\n", next->function, next->caller, next->stackFrame, next->startAddress, next->endAddress);
    for(int i = 0; i < next->stacksize / 4; i++) {
        printf("%p-%p: %08x\n", next->startAddress + i * 4 - 1, next->startAddress - 4 *(i+1), *((int*)(next->final + i * 4)));
    }
    counter += 1;
}
/*
    Stores stack information sorted by start time
    @param file File Descriptor
*/
struct node* startTime(int file) {
    struct node* head = malloc(sizeof(struct node));
    struct node* readin = malloc(sizeof(struct node));
    head->final = malloc(sizeof(int*) * head->stacksize);
    read(file, head, sizeof(struct node));
    read(file, head->final, head->stacksize);
    while(read(file, readin, sizeof(struct node)) == sizeof(struct node)) {
        read(file, readin->final, head->stacksize);
        struct node* current = head;
        if(readin->start_time->tv_nsec < head->start_time->tv_nsec) {
            readin->next = head;
            head = readin;
        }
        while(current->next != NULL && current->next->start_time->tv_nsec < readin->start_time->tv_nsec) {
            readin->next = current->next;
            current->next = readin;
        }
        readin = malloc(sizeof(struct node));
    }
    return head;
}

/*
    Stores stack information sorted by finish time
    @param file File Descriptor
*/
struct node* endTime(int file) {
    struct node* head = malloc(sizeof(struct node));
    struct node* readin = malloc(sizeof(struct node));
    head->final = malloc(sizeof(int*) * head->stacksize);
    read(file, head, sizeof(struct node));
    read(file, head->final, head->stacksize);
    while(read(file, readin, sizeof(struct node)) == sizeof(struct node)) {
        read(file, readin->final, head->stacksize);
        struct node* current = head;
        if(readin->end < head->end) {
            readin->next = head;
            head = readin;
        }
        while(current->next != NULL && current->next->end < readin->end) {
            readin->next = current->next;
            current->next = readin;
        }
        readin = malloc(sizeof(struct node));
    }
    return head;
}


int main(int argc, char** argv) {
    struct node* head = (struct node*) malloc(sizeof(struct node));             //Declaring variable for future sorted list 
    struct node* next;                                                          //Declaring variable to be used for printing
    int file = open("stack.bin", O_RDWR);   
    if(argc > 1 && strcmp(argv[1], "-s") == 0) {                                //Control structure to find out how data is sorted
        head = startTime(file);
    } else if(argc > 1 && strcmp(argv[1], "-f") == 0) {
        head = endTime(file);
    } else {        
        read(file, head, sizeof(struct node));                                  //Reads in file unsorted for debugging
        head->final = malloc(sizeof(int*) * head->stacksize);
        read(file, head->final, head->stacksize);
        head->next = (struct node*)malloc(sizeof(struct node));
        next = head->next;
        while(read(file, next, sizeof(struct node)) == sizeof(struct node)) {
            next->next = (struct node*)malloc(sizeof(struct node));
            next->final = malloc(sizeof(int*) * head->stacksize);
            read(file, next->final, next->stacksize);
            next = next->next;
        }
    }
    next = head;                   //Iterates through linked list and prints out each node
    while(next != NULL) {
        print(next);
        next = next->next;
    }

    return 0;
}