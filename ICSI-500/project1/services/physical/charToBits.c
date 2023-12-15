#include <stdio.h>

int main(int argc, char *argv[]) {
  char ch;
  if(argc > 1) {
    ch = *argv[1];
    for(int i = 6; i > -1; i--) {
      printf("%d", (ch >> i) & 1);
    }
  } else {
    while(scanf("%c", &ch) != EOF) {
      if(ch == '\0') {
        break;
      }
      for(int i = 6; i > -1; i--) {
        printf("%d", (ch >> i) & 1);
      }
    }
  }
  printf("%c", '\0');
  return 0;
}
