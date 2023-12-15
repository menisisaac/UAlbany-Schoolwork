#include <stdio.h>

int readInput(FILE* fn, char* bits) {
  char ch;
  int index = 0;
  while((ch = fgetc(fn)) != EOF & index < 8) {
    bits[index++] = ch;
  }
  return index;
}
