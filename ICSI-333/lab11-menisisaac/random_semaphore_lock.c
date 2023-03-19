#include <stdlib.h>
#include <pthread.h>
#include <stdio.h>
#include <semaphore.h>

sem_t semaSum;
int sum = 0;

void *firstHalf(int randoms[]) {
    int localSum = 0;
    for(int i = 0; i < 500000; i++) {
        localSum += randoms[i];
    }
    sem_wait(&semaSum);
    sum += localSum;
    sem_post(&semaSum);
}

void *secondHalf(int randoms[]) {
    int localSum = 0;
    for(int i = 500000; i < 1000000; i++) {
        localSum += randoms[i];
    }
    sem_wait(&semaSum);
    sum += localSum;
    sem_post(&semaSum);
}


int main(void) {
    sem_init(&semaSum, 0, 0);
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
    printf("Sum: %i\n", sum);
    exit(0);
}