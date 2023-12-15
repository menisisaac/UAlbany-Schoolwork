#include <stdio.h>

void fromFrame(char* frame, char* bits) {

  for(int i = 0; i < 64; i++) {
    bits[i] = frame[i + 3];
  }
  
}
