#include <stdio.h>
#include "stack.h"
#define DEFAULTOFFSET 32

/*
* Checks the print_stack function
* @param value: Message for print_stack
*/
void f2(char *value) {
    print_stack(value, DEFAULTOFFSET);
}

/*
* Checks the print_stack function
* @param value: A postive integer
*/
void f1(unsigned int value) {
    print_stack("in f1", value);
    char* temp = "ABCD";
    f2(temp);
}

/*
* Tests the print_stack function
*/
int main(void) {
    print_stack("in main", DEFAULTOFFSET);
    f1(10);
    return 0;
}