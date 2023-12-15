#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>
#include <limits.h>
#include <string.h>
#include <sys/wait.h>
#include "encDec.h"
#define RELATIVE_PATH "services/bin/"

void absolutePath(char cwd[], char service[]) {
  strcpy(cwd, RELATIVE_PATH);
  strcpy(cwd + strlen(cwd), service);
}

int main(int argc, char *argv[]) {
  char filename[512];
  strcpy(filename, argv[1]);
  int filenameLength = strlen(filename);
  pid_t pid; 
  int servicePipe[2];
  int outServicePipe[2];
  if (pipe(outServicePipe) == -1) {
    fprintf(stderr, "Failed to create service pipe");
  }

  if (pipe(servicePipe) == -1) {
    fprintf(stderr, "Failed to create service pipe");
    return 1;
  }

  if((pid = fork()) == 0) {
    dup2(servicePipe[0], 0);
    dup2(outServicePipe[1], 1);
    close(servicePipe[0]);
    close(servicePipe[1]);
    close(outServicePipe[0]);
    close(outServicePipe[1]);
    execl("pipeTools", "pipeTools", filename, NULL);
    fprintf(stderr, "Failed");
  } else {
    char tobits[PATH_MAX];
    char parity[PATH_MAX];
    char framing[PATH_MAX];
    char deframing[PATH_MAX];
    char removingParity[PATH_MAX];
    char backToChar[PATH_MAX];
    char toUpperCase[PATH_MAX];
    char outputToFile[PATH_MAX];
    char name1[] = "charToBits";
    char name2[] = "addParity";
    char name3[] = "toFrame";
    char name4[] = "fromFrame";
    char name5[] = "validateRemoveParity";
    char name6[] = "bitsToChar";
    char name7[] = "toUpperCase";
    char name8[] = "outputToFile";
    absolutePath(tobits, "charToBits");
    absolutePath(parity, "addParity");
    absolutePath(framing, "toFrame");
    absolutePath(deframing, "fromFrame");
    absolutePath(removingParity, "validateRemoveParity");
    absolutePath(backToChar, "bitsToChar");
    absolutePath(toUpperCase, "toUpperCase");
    absolutePath(outputToFile, "outputToFile");
    //Deframe and process
    int size = 4;
    write(servicePipe[1], &size, sizeof(size));
    write(servicePipe[1], &deframing, strlen(deframing) + 1);
    write(servicePipe[1], &name4, strlen(name4) + 1);
    write(servicePipe[1], &removingParity, strlen(removingParity) + 1);
    write(servicePipe[1], &name5, strlen(name5) + 1);
    write(servicePipe[1], &backToChar, strlen(backToChar) + 1);
    write(servicePipe[1], &name6, strlen(name6) + 1);
    write(servicePipe[1], &toUpperCase, strlen(toUpperCase) + 1);
    write(servicePipe[1], &name7, strlen(name7) + 1);
    close(servicePipe[0]);
    int fileToServices[2];
    if(pipe(fileToServices) == -1) {
      fprintf(stderr, "Pipe Failed to be created");
    }
    //Output to file
    if ((pid = fork()) == 0) {
      dup2(outServicePipe[0], 0);
      dup2(fileToServices[1], 1);
      close(fileToServices[1]);
      close(fileToServices[0]);
      close(outServicePipe[0]);
      close(outServicePipe[1]);
      strcpy(filename + filenameLength, ".outf");
      execl(outputToFile, name8, filename, NULL);
      fprintf(stderr, "Failed");
  } else {
    int consumerToServices[2];
    if(pipe(consumerToServices) == -1) {
      fprintf(stderr, "Pipe Failed to be created");
    }
    int servicesToOutput[2];
    if(pipe(servicesToOutput) == -1) {
      fprintf(stderr, "Pipe Failed to be created");
    }
    //Uppercase to frame
    if((pid = fork()) == 0) {
      dup2(consumerToServices[0], 0);
      dup2(servicesToOutput[1], 1);
      close(consumerToServices[0]);
      close(consumerToServices[1]);
      close(servicesToOutput[0]);
      close(servicesToOutput[1]);
      execl("pipeTools", "pipeTools", filename, NULL);
      fprintf(stderr, "Now Failed");
    } else {
      close(fileToServices[1]);
      close(servicesToOutput[1]);
      size = 3;
      write(consumerToServices[1], &size, sizeof(size));
      write(consumerToServices[1], &tobits, strlen(tobits) + 1);
      write(consumerToServices[1], &name1, strlen(name1) + 1);
      write(consumerToServices[1], &parity, strlen(parity) + 1);
      write(consumerToServices[1], &name2, strlen(name2) + 1);
      write(consumerToServices[1], &framing, strlen(framing) + 1);
      write(consumerToServices[1], &name3, strlen(name3) + 1);
      close(consumerToServices[0]);
    //Services to output
    if((pid = fork()) == 0) {
      dup2(servicesToOutput[0], 0);
      close(servicesToOutput[0]);
      strcpy(filename + filenameLength, ".chck");
      execl(outputToFile, name8, filename, NULL);
      fprintf(stderr, "Failed");
    } else {
      char ch; 
      char term = '\0';
      while(scanf("%c", &ch) != EOF) {
        if(ch == '\0') {
          break;
        }
        write(servicePipe[1], &ch, sizeof(ch));
      }
      write(servicePipe[1], &term, sizeof(term));
      close(servicePipe[1]);
      while(read(fileToServices[0], &ch, sizeof(ch)) != 0) {
        if(ch == '\0') {
          break;
        }
        write(consumerToServices[1], &ch, sizeof(ch));
      }
      write(consumerToServices[1], &term, sizeof(term));

      }
    }
  }
  }
  return 0;
}
