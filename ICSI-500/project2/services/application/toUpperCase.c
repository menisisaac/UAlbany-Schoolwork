#include <stdio.h>

int toUpperCase(char ch) {
  if (ch >= 'a' & ch <= 'z') {
    ch = ch - 32;
  }
  return ch;
}
