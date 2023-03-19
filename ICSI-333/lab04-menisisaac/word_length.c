#include <stdio.h>
#include <string.h>

int main() {
    printf("Enter the string:");
    char c[100];
    scanf("%s", c);
    printf("Length with standard function %i\n", strlen(c));
    char *pointer = &c;
    int i = 0;
    while(*pointer != '\0') {
        i++;
        pointer++;
    }
    printf("Length using pointers %i", i);
}
