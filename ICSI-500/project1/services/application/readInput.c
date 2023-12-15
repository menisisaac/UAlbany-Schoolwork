#include <stdio.h>

int main() {
  char fn[512];
  scanf("%s", fn);

  FILE* fp = fopen(fn, "r");
  if(fp == NULL) {
    fprintf(stderr, "Input data could not be read");
  }
  char ch;
  while((ch = fgetc(fp)) != EOF) {
    printf("%c", ch);
  }
  printf("%c", '\0');
  fclose(fp);
  return 0;
}
