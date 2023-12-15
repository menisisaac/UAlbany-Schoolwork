#include <stdio.h>
#include "services/physical/charToBits.c"
#include "services/application/readInput.c"
#include "hamming.c"
#include <string.h>



int main(int argc, char** argv) {
    char ch; 
    char eight[8];
    char bits[57];
    bits[56] = '0';
    int i = 0;
    while(i < strlen(argv[1])) {
        char encoded[63];
        for(int j = 0; j < 8; j++) {
            if(i < strlen(argv[1])) {
                charToBits(argv[1][i++], &bits[j * 7]);
            } else {
                charToBits('0', &bits[j*7]);
            }
        }
        hammingEncode(bits, encoded);
        if(encoded[i % 56] == '0') {
            encoded[i % 56] = '1';
        } else {
            encoded[i % 56] = '0';
        }
        printf("Error stimulated at: %d\n", i % 56 + 1);
        hammingDecode(encoded);
    }



}