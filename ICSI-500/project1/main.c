#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>
#include <limits.h>
#include <string.h>
#include <sys/wait.h>

int main(int argc, char *argv[]) {
  pid_t producer;
  pid_t consumer;
  int toConsumer[2];
  int toProducer[2];
  char errors = '0';
  if (argc == 3) {
    if (strcmp(argv[2], "-e") == 0) {
      errors = '1';
    }
  }
  if (pipe(toConsumer) == -1) {
    fprintf(stderr, "toConsumer pipe Failed");
    return 1;
  }
  if (pipe(toProducer) == -1) {
    fprintf(stderr, "toProducer pipe Failed");
    return 1;
  }

  if((producer = fork()) == 0) {
    dup2(toProducer[0], 0);
    dup2(toConsumer[1], 1);
    close(toProducer[0]);
    close(toProducer[1]);
    close(toConsumer[0]);
    close(toConsumer[1]);
    execl("producer", "producer", argv[1], &errors, NULL);
    fprintf(stderr, "Producer Failed");
  } else {
    if((consumer = fork()) == 0) {
      dup2(toConsumer[0], 0);
      dup2(toProducer[1], 1);
      close(toProducer[0]);
      close(toProducer[1]);
      close(toConsumer[0]);
      close(toConsumer[1]);
      execl("consumer", "consumer", argv[1], &errors, NULL);
      fprintf(stderr, "Consumer Failed");
    } else {
      close(toProducer[0]);
      close(toProducer[1]);
      close(toConsumer[0]);
      close(toConsumer[1]);
    }
  }
  return 0;

}
