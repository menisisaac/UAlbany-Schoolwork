#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char* argv[]) {

  char filename[512];
  strcpy(filename, argv[1]);
  strcpy(filename + strlen(filename), ".inpf");
  int n;
  char ch;
  char newLine = '\n';
  read(0, &n, sizeof(n));
  char program[n][512];
  char programAddy[n][4096];
  for(int i = 0; i < n; i++) {
    int index = 0;
    while(read(0, &ch, sizeof(ch)) != 0) {
      if(ch == '\0') {
        programAddy[i][index] = '\0';
        break;
      } else {
        programAddy[i][index] = ch;
        index++;
      }
    } 
    index = 0;
    while(read(0, &ch, sizeof(ch)) != 0) {
      if(ch == '\0') {
        program[i][index] = '\0';
        break;
      } else {
        program[i][index] = ch;
        index++;
      }
    }
  }

  int init[2];
  pipe(init);

  int fd[n][2];
  for(int i = 0; i < n - 1; i++) {
    pipe(fd[i]);
  }
  pid_t pid[n];
  for(int i = 0; i < n; i++) {
    if((pid[i] = fork()) == 0) {
      if(i != 0) {
        dup2(fd[i - 1][0], 0);
        close(fd[i - 1][0]);
        close(fd[i - 1][1]);
        close(init[1]);
        close(init[0]);
      } else {
        close(init[1]);
        dup2(init[0], 0);
        close(init[0]);
      }

      if(i != n - 1) {
        dup2(fd[i][1], 1);
        close(fd[i][1]);
        close(fd[i][0]);
      } 
      execl(programAddy[i], program[i], NULL);
      fprintf(stderr, "Execl failed");
      break;
    } else {
      
        if(i == n - 1) {
          FILE* write_file = fdopen(init[1], "w");
          close(init[0]);
          
          char ch;
          while(read(0, &ch, sizeof(ch)) > 0) {
            if(ch == '\0') {
              fprintf(write_file, "%c", '\0');
              break;
            }
            fprintf(write_file, "%c", ch);
          }
          if(strcmp(program[0], "readInput") == 0) {
            fprintf(write_file, "%s", filename);
          }
          fclose(write_file);
          for(int i = 0; i < n - 1; i++) {
            close(fd[i][1]);
            close(fd[i][0]);
          }
        }
      }
  }
}
