#include <time.h>
#include <unistd.h>
#include <stdio.h>
#include <fcntl.h>
#include <stdlib.h>
#include <string.h>

void __cyg_profile_func_enter(void *this_fn, void *call_site);
void __cyg_profile_func_exit(void *this_fn, void *call_site);

/*
    Represents the Stack Frame structure includes stack
*/
struct node {
    float time;
    void* function;
    void* caller;
    void* stackFrame;
    void* startAddress;
    void* endAddress;
    int stacksize;
    void* initial;
    void* final;

    struct node *next;
};

int startTime;                      //Allows calculation of time 
int endTime;                        //Allows calculation of time
struct node* head= NULL;            
struct node* current;

/*
    Calculates the runtime of function with global variables
*/
double createReport() {             
    double running_time = (float) (endTime - startTime) / CLOCKS_PER_SEC;
    startTime = 0;
    endTime = 0;
    return running_time;
}


/*
    Goes through linked list writing each node to file
    Used same structure as lab 5 however it didn't work here
*/
void writeToFile() {
    FILE* f = fopen("stack.bin", "ab");
    current = head;
    //while(current != NULL) {
        //fwrite(current, sizeof(struct node), 1, f);
        //fwrite(current->initial, sizeof(current->stacksize * sizeof(int)), 1, f);
        //fwrite(current->final, sizeof(current->stacksize * sizeof(int)), 1, f);
        //current = current->next;
    //}
    fclose(f);
}

/*
    A test to show that my analysis of the stack works, 
    prints everything stackviz prints
*/
void testContents() {
    printf("Function: %p Caller: %p Frame Pointer: %p\nStack Frame: %p - %p Time: %f\n\n", current->function, current->caller, current->stackFrame, current->startAddress, current->endAddress, current->time);
    printf("%i\n", current->stacksize);
    for(int i = 0; i < current->stacksize - 1; i += 4) {
        printf("%p-%p: %08x\n", current->startAddress + i - 1, current->startAddress - 4 * (i + 1), *((int*)(current->initial + i)));
    }
}

/*
    Checks for Head of linked list, if starts a new one
    Populates node with all data except time and final contents
*/
void __cyg_profile_func_enter(void *this_fn, void *call_site) {
    if(head == NULL) {
        head = malloc(sizeof(struct node));
        current = head;
    } else {
        current->next = malloc(sizeof(struct node));
        current = current->next;
        current->next = NULL;
    }
    current->function = this_fn;
    current->caller = call_site;
    current->stackFrame = __builtin_frame_address(1);
    current->startAddress = __builtin_frame_address(1) + 2 * sizeof(void*);
    current->endAddress = __builtin_frame_address(0) + 4 * sizeof(void*);
    current->stacksize = current->startAddress - current->endAddress;
    current->initial = malloc(sizeof(current->stacksize) * sizeof(void*));
    memcpy(current->initial, current->endAddress, (current->stacksize));
    startTime = clock();
}

/*
    Gets final time and triggers time calculation, 
    gets final contents, and triggers the file write,
    Shows the analysis of stack frame
*/
void __cyg_profile_func_exit(void *this_fn, void *call_site) {
    endTime = clock();
    current->time = createReport();
    current->final = malloc(sizeof(current->stacksize) * sizeof(void*));
    memcpy(current->final, current->endAddress, (current->stacksize));
    testContents();
    writeToFile();
}

