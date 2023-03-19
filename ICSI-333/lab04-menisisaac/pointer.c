#include <stdio.h>

int main() {
    int v1;
    int v2;
    int *p1 = &v1;
    int *p2 = &v2;
    v1 = 10;
    *p2 = 4;
    printf("V1: %i V2: %i P1: %i P2: %i", v1, v2, *p1, *p2);
}
