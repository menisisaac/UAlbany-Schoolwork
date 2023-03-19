#include <stdlib.h>
#include <pthread.h>
#include <stdio.h>

int sum = 0;

void *firstHalf(int randoms[]) {
    for(int i = 0; i < 500000; i++) {
        sum += randoms[i];
    }
}

void *secondHalf(int randoms[]) {
    for(int i = 500000; i < 1000000; i++) {
        sum += randoms[i];
    }
}


int main(void) {
    srand(time(NULL));
    int randomInts[1000000];
    pthread_t thread_ids[2];
    for(int i = 0; i < 1000000; i++) {
        randomInts[i] = rand() % 10;
    }
    pthread_create(&thread_ids[0], NULL, firstHalf, randomInts);
    pthread_create(&thread_ids[1], NULL, secondHalf, randomInts);
    for(int i = 0; i < 2; i++) {
        pthread_join(thread_ids[i], NULL);
    }
    printf("%i", sum);
    exit(0);
}