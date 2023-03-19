#include <stdio.h>

int main() {
    int one;
    int two;
    printf("Input first number: \n");
    scanf("%i", &one);
    printf("Input second number: \n");
    scanf("%i", &two);
    int quotient = one / two;
    int remainder = one % two;
    printf("Quotient: %i Remainder: %i\n", quotient, remainder);
    return 0;
}
