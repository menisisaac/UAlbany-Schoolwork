#include <stdio.h>
#include <string.h>

int main(int argc, char *argv[]) {
  FILE* fp = fopen(argv[1], "w");
  if(fp == NULL) {
    fprintf(stderr, "Output data could not be written");
  }
  char ch;
  while((scanf("%c", &ch) != EOF)) {
    if(ch == '\0') {
      break;
    }
    fputc(ch, fp);
    printf("%c", ch);
  }
  printf("%c", '\0');
  fclose(fp);
  return 0;
}
