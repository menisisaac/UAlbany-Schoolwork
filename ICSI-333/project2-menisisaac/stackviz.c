#include <stdio.h>
#include <stdlib.h>

/*
    Same node as inst.c
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


/*
    Opens stack.bin file, reads linked list nodes in, prints report
*/
int main(void) {
    struct node stack;
    FILE* file = fopen("stack.bin", "ab");
    fread(&stack, sizeof(struct node) + sizeof(double), 1, file);
    printf("function: %p, caller: %p, frame pointer: %p\nstack frame: %p-%p, time: %f", 
    stack.function, stack.caller, stack.stackFrame, stack.endAddress, stack.startAddress, stack.time);
    return 0;
}