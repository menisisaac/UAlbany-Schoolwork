#include <stdio.h>


int main(int argc, char* argv[]) {
  int count = 0;
  char ch;
  while(scanf("%c", &ch) != EOF) {
    if (ch == '\0') {
      break;
    }
    count = 0;
    if(ch == '1') {
      count = 1;
    }
    printf("%c", ch);
    for(int i = 1; i < 7; i++) {
      scanf("%c", &ch);
      printf("%c", ch);
      if(ch == '1') {
        count++;
      }
    }
    int parity;
    if(count % 2 == 0) {
      parity = '1';
    } else {
      parity = '0';
    }
    printf("%c", parity);
  }
  printf("%c", '\0');
  return 0;
}
