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
  int inServicePipe[2];
  int outServicePipe[2];
  if (pipe(inServicePipe) == -1) {
    fprintf(stderr, "Failed to create service pipe");
    return 1;
  }
  if (pipe(outServicePipe) == -1) {
    fprintf(stderr, "Failed to create service pipe");
    return 1;
  }
  
  if((pid = fork()) == 0) {
    dup2(inServicePipe[0], 0);
    dup2(outServicePipe[1], 1);
    close(inServicePipe[0]);
    close(inServicePipe[1]);
    close(outServicePipe[0]);
    close(outServicePipe[1]);
    execl("pipeTools", "pipeTools",  filename, NULL);
    fprintf(stderr, "Failed");
  } else {
    char getfile[PATH_MAX];
    char tobits[PATH_MAX];
    char parity[PATH_MAX];
    char framing[PATH_MAX];
    char deframing[PATH_MAX];
    char removingParity[PATH_MAX];
    char backToChar[PATH_MAX];
    char outputToFile[PATH_MAX];
    char name[] = "readInput";
    char name1[] = "charToBits";
    char name2[] = "addParity";
    char name3[] = "toFrame";
    char name4[] = "fromFrame";
    char name5[] = "validateRemoveParity";
    char name6[] = "bitsToChar";
    char name7[] = "outputToFile";
    absolutePath(getfile, "readInput");
    absolutePath(tobits, "charToBits");
    absolutePath(parity, "addParity");
    absolutePath(framing, "toFrame");
    absolutePath(deframing, "fromFrame");
    absolutePath(removingParity, "validateRemoveParity");
    absolutePath(backToChar, "bitsToChar");
    absolutePath(outputToFile, "outputToFile");
    int size = 4;

    // Pipe 1 Go to Pipetools
    write(inServicePipe[1], &size, sizeof(size));
    write(inServicePipe[1], &getfile, strlen(getfile) + 1);
    write(inServicePipe[1], &name, strlen(name) + 1);
    write(inServicePipe[1], &tobits, strlen(tobits) + 1);
    write(inServicePipe[1], &name1, strlen(name1) + 1);
    write(inServicePipe[1], &parity, strlen(parity) + 1);
    write(inServicePipe[1], &name2, strlen(name2) + 1);
    write(inServicePipe[1], &framing, strlen(framing) + 1);
    write(inServicePipe[1], &name3, strlen(name3) + 1);
    close(inServicePipe[1]);
    close(inServicePipe[0]);
    // Pipe 2 Go from last Service to producer
    close(outServicePipe[1]);
    if((pid = fork()) == 0) {
      dup2(outServicePipe[0], 0);
      close(outServicePipe[0]);
      strcpy(filename + filenameLength, ".binf");
      execl(outputToFile, name7, filename, NULL);
      fprintf(stderr, "Error outputting to file");
    } else {
        int toServicesTwo[2];
        int servicesToOutput[2];
        if (pipe(toServicesTwo) == -1) {
          fprintf(stderr, "Failed to create service pipe");
          return 1;
        }
        if (pipe(servicesToOutput) == -1) {
          fprintf(stderr, "Failed to create output pipe");
          return 1;
        }
        if((pid = fork()) == 0) {
          dup2(toServicesTwo[0], 0);
          dup2(servicesToOutput[1], 1);
          close(toServicesTwo[0]);
          close(toServicesTwo[1]);
          close(servicesToOutput[0]);
          close(servicesToOutput[1]);      
          execl("pipeTools", "pipeTools", filename, NULL);
          fprintf(stderr, "Failed");
        } else {
            close(servicesToOutput[1]);
            if((pid = fork()) == 0) {
              dup2(servicesToOutput[0], 0);
              close(servicesToOutput[0]);
              strcpy(filename + filenameLength, ".done");
              execl(outputToFile, name7, filename, NULL);
              fprintf(stderr, "Error outputting to file");
            } else {
                size = 3;
                if(*argv[2] == '1') {
                  size = size + 1;
                }
                write(toServicesTwo[1], &size, sizeof(size));
                char error[] = "services/bin/errorSimulation";
                char errorName[] = "errorSimulation";
                if(*argv[2] == '1') {
                  write(toServicesTwo[1], &error, strlen(error) + 1);
                  write(toServicesTwo[1], &errorName, strlen(errorName) + 1);
                }
                write(toServicesTwo[1], &deframing, strlen(deframing) + 1);
                write(toServicesTwo[1], &name4, strlen(name4) + 1);
                write(toServicesTwo[1], &removingParity, strlen(removingParity) + 1);
                write(toServicesTwo[1], &name5, strlen(name5) + 1);
                write(toServicesTwo[1], &backToChar, strlen(backToChar) + 1);
                write(toServicesTwo[1], &name6, strlen(name6) + 1);
                char ch;
                char term = '\0';
                while(scanf("%c", &ch) != EOF) {
                  if(ch == '\0') {
                    write(toServicesTwo[1], &term, sizeof(term));
                    break;
                  }
                  write(toServicesTwo[1], &ch, sizeof(ch));
                }
            }
        }
    }
  }
  return 0;
}
