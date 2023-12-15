#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int main() {
  char syn1;
  char syn2;
  char length;
  char data[64];
  while(scanf("%c", &syn1) != EOF) {
    if(syn1 == '\0') {
      break;
    }
    if (syn1 != 22) {
      fprintf(stderr, "Frame Formatting Error First");
    }
    if (scanf("%c", &syn2) != 1 || syn2 != 22) {
      fprintf(stderr, "Frame Formatting Error Second");
    }
    if (scanf("%c", &length) != 1) {
      fprintf(stderr, "Frame Formatting Error Third");
    }
    for(int i = 0; i < 64; i++) {
      scanf("%c", data + i);
    }
    printf("%c", syn1);
    printf("%c", syn1);
    printf("%c", length);
    for(int i = 0; i < length; i++) {
      if(rand() % 20 == 0) {
        if(data[i] == '0') {
          data[i] = '1';
        } else {
          data[i] = '0';
        }
      }
    }

    for(int i = 0; i < 64; i++) {
      printf("%c", data[i]);
    }
  }
  printf("%c", '\0');
  return 0;

}
