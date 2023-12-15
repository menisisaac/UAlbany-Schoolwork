#include <stdio.h>
#include <stdlib.h>

int main() {
  char bits[8];
  while(scanf(" %c", &bits[1]) != EOF) {
    if(bits[1] == '\0') {
      break;
    }
    bits[0] = '0';
    for(int j = 2; j < 8; j++) {
      scanf(" %c", bits + j);
    }
    char ch = strtol(bits, 0, 2);
    printf("%c", ch);
  }
  printf("%c", '\0');
}
