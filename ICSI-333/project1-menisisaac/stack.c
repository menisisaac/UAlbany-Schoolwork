#include <stdio.h>
register void *sp asm ("rsp");

/*
* Prints from the contents of offset bytes to 
* stack  pointer in form of different data types
* @param message: Custom debugging message
* @param offset: Number of bytes start above the stack pointer
*/
void print_stack(char* message, int offset) {
    unsigned int *intpoint;
    unsigned char *charpoint;
    unsigned long *pointer;
    fprintf(stderr, "Stack (offset: %i): %s\n", offset, message);
    fprintf(stderr, "Frame Pointer: %018#x\n", __builtin_frame_address(0));
    fprintf(stderr, "Stack Pointer: %018#x\n", sp);
    for(int i = offset; i > -1; i--) {                                      //Decrement from max byte interval to the top of stack
        intpoint = (unsigned int*)(sp + i);
        charpoint = (unsigned char*)(sp + i);
        pointer = (unsigned long*)(sp + i);
        fprintf(stderr, "%018#x: uchar(%03u) uint(%010u) pointer(%018p)\n", //Print different datatype values in the bytes location with correct padding
                sp + i, *charpoint, *intpoint, *pointer);
    }
}