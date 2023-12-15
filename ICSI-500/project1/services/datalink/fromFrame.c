#include <stdio.h>

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
    for(int i = 0; i < length; i++) {
      printf("%c", data[i]);
    }
  }
  printf("%c", '\0');
  return 0;

}
