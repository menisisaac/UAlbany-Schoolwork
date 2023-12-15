#include <stdio.h>
#include <string.h>

unsigned int generateCRC(char* letters) {
    unsigned int crc = 0xFFFFFFFF;
    unsigned int* bytes = (unsigned int*)letters;

    for(int i = 0; i < strlen(letters); i++) {
        crc ^= bytes[i];

        for(int j = 0; j < 8; j++) {
        crc = (crc >> 1) ^ ((crc & 1) ? 0xEDB88320 : 0);
        }
    }
    return crc ^ 0xFFFFFFFF;
}




int main() {
    char* input = "hello my name is isaac";
    unsigned int crc = generateCRC(input);
    char* inputWrong = "Hello my name is isaac";
    unsigned int crcWrong = generateCRC(inputWrong);
    printf("Comparing original crc with one letter changed: %d", crc == crcWrong);
}