#include <stdio.h>
#include <sys/types.h>
#include <dirent.h>
#include <ctype.h>

void fileProcess(char *filename) {
    if(checkName(filename) == 1) {
        int fd = open(name, O_WRONLY | O_CREAT, S_IWUSR); 
        
    }
}

int checkName(char *filename) {
    char test[12] = {'n', 'u', 'm', 'b', 'e', 'r', 's', '_', '.', 't', 'x', 't'};
    for(int i = 0; i < 8; i++) {
        if(test[i] != filename[i]) {
            return 0;
        }
    }
    for(int i = 8; i < 12; i++) {
        if(filename[i] < '0' || filename[i] > '9') {
            return 0;
        }
    }
    for(int i = 12; i < 16; i++) {
        if(test[i - 4] != filename[i]) {
            return 0;
        }
    }
    return 1;
}



int main(void) {
    DIR *directory = opendir("./");
    if (directory == NULL) {
        fprintf(stderr, "Directory not found");
    }
    struct dirent *file = NULL;
    while((file = readdir(directory))!= NULL) {
        if(file->d_namlen == 16) {   
            fileProcess(file->d_name);                
        }
    }




    return 0;
}