#include <stdio.h>

int main() {
  char bits[8];
  while(scanf("%c", bits) != EOF) {
    if(bits[0] == '\0') {
      break;
    }
    int count = 0;
    if(bits[0] == '1') {
      count = 1;
    }
    for(int i = 1; i < 8; i++) {
      scanf(" %c ", bits + i);
      if(bits[i] == '1') {
        count += 1;
      }
   }
    if(count % 2 != 1) {
      fprintf(stderr, "Parity bit error");
    }
    for(int i = 0; i < 7; i++) {
      printf("%c", bits[i]);
    }
  }
  printf("%c", '\0');
}
