#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include <fcntl.h>
#include <unistd.h>

int main(void) {
    int random[100];
    srand(time(NULL));
    int sum = 0;
    for(int i = 0; i < 100; i++) {
        random[i] = rand() % 100;
        sum += random[i];
    }
    printf("The sum is %i\n", sum);
    char name[17];
    snprintf(name, 17, "numbers_%04i.txt", sum);
    printf(name);
    int fd = open(name, O_WRONLY | O_CREAT, S_IWUSR); 
    for(int i = 0; i < 100; i++) {
        write(fd, *random + i, sizeof(random[i]));
    }
    close(fd);
    return 0;
}