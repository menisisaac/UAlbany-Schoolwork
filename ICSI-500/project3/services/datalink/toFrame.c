#include <stdio.h>


void toFrame(char* bits, char* frame) {
  char syn = 22;
  int length = strlen(bits);
  int index = 4;
  char frame[67] = {0};
  frame[0] = 22;
  frame[1] = 22;
  frame[2] = length;
  for(int i = 0; i < 64; i++) {
    frame[i + 3] = bits[i];
  }
  

}
