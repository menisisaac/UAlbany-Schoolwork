#include <stdio.h>
#include <stdlib.h>

int main(void) {
    FILE* fileDescriptor = fopen("input.txt", "r");
    if(fileDescriptor != NULL) {
        char c = ' ';
        do {
            c = fgetc(fileDescriptor);
            if(c == ' ' || c == '\t') {
                printf("\n");
                while(1) {
                    c = fgetc(fileDescriptor);
                    if(c == -1) {
                        break;
                    }
                    if(c != ' ') {
                        break;
                    } else if(c == -1) {
                        break;
                    }
                }
            }
            if(c == -1) {
                break;
            }
            if(c != '\n') {
                printf("%c", c);
            }
        } while(1);
        fclose(fileDescriptor);
    } else {
        printf("File could not open");
        return -1;
    }
    return 0;
}