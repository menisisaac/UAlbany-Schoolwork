#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <stdio.h>
#include <string.h>

int main(int argc, char** argv) {
    pid_t pid = fork();
    if(pid == 0) {
        if(argc > 1) {
            const int size = argc;
            char* args[size];
            args[argc - 1] = NULL;
            for(int i = 1; i < argc; i++) {     //Copy program name and arguments to new array
                args[i - 1] = argv[i];
            }
            char* programPath = malloc(6 + strlen(argv[0]));    //Allocate space for string addition
            char* path = "/bin/";
            strcpy(programPath, path);
            strcat(programPath, argv[1]);
            execv(programPath, args);
            free(programPath);
        }
        return 0;
    } else {
        int status;
        waitpid(pid, &status, 0);
        fprintf(stderr, "Exit code was %i", WEXITSTATUS(status));
    }
}