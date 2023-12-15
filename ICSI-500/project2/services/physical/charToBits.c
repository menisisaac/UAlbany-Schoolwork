#include <stdio.h>

int charToBits(char ch, char* bits) {
      for(int i = 6; i > -1; i--) {
        bits[i] = (char)((ch >> i) & 1) + 48;
      }
}
