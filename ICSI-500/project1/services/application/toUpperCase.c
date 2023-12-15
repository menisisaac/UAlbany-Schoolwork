#include <stdio.h>

int main(int argc, char *argv[]) {
  char ch;
  while(scanf("%c", &ch) != EOF) {
    if(ch == '\0') {
      break;
    }
    if (ch >= 'a' & ch <= 'z') {
      ch = ch - 32;
    }
    printf("%c", ch);
  }
  printf("%c", '\0');
  return 0;
}
