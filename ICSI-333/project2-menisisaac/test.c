#include <stdio.h>
#include <unistd.h>

void f1(int x) {
  for (int i = 0; i < 100000; i++) { // Just a busy loop to waste some CPU time
    asm("");
  }

  char buffer[10] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}; // Local array that is updated later in the function
  int local_int = 0x89abcdef; // a recognizable local variable on the stack

  for (int i = 0; i < sizeof(buffer); i++) { // Update and print the buffer
    buffer[i] = x;
    printf("%u ", buffer[i]);
  }
  printf("\n");

  if (x > 1) { // Recursively call f1()
    f1(x - 1);
  }
}

int main(void) {
  f1(3);
  f1(1);
  return 0;
}