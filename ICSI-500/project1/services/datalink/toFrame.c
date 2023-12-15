#include <stdio.h>


int main() {
  char syn = 22;
  int length;
  int index = 4;
  char frame[67] = {0};

  while(scanf("%c", frame + 3) != EOF) {
    if(frame[3] == '\0') {
      break;
    }
    frame[0] = syn;
    frame[1] = syn;
    length = 1;
    index = 4;
    while(index < 67 && scanf(" %c ", frame + index) != EOF) {
      index++;
      length++;
    }
    frame[2] = length;
    for(int i = 0; i < length + 3; i++) {
      printf("%c", frame[i]);
    } 
    for(int i = length + 3; i < 67; i++) {
      printf("%c", '0');
    }
  }
  printf("%c", '\0');
  return 0;

}
