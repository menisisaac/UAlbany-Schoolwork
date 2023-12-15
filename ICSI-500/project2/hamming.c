#include <stdio.h>
#include <math.h>



void hammingDecode(char* frame) {
    char parity[6];

    for(int i = 0; i < 6; i++) {
        int num = (int)pow(2, i);

        int start = num - 1;
        int count = 0;
        while(start < 63) {
            for(int j = 0; j < num; j++) {
                if(start == 63) {
                    break;
                }
                if(i == 1) {
                }
                if(frame[start++] == '1') {
                    count += 1;
                }
            }
            for(int j = 0; j < num; j++) {
                if(start == 63) {
                    break;
                }
                start++;
            }
        }
        if(count % 2 == 0) {
            parity[i] = '0';
        } else {
            parity[i] = '1';
        }

    }
    int count = 0;
    for(int i = 0; i < 6; i++) {
        if(parity[i] == '1') {
            count += (int)pow(2, i);
        }
    }
    if(count != 0) {
        printf("Error in %d\n", count);       
    } 

}

void hammingEncode(char* message, char* encoded) {
    char parity[6];
    int power = 0;
    int currPos = 0;
    for(int i = 0; i < 63; i++) {
        if(i + 1 != (int)pow(2, power)) {
            encoded[i] = message[currPos++];
        } else {
            power++;
            encoded[i] = '0';
        }
    }
    for(int i = 0; i < 6; i++) {
        int num = (int)pow(2, i);
        int start = num - 1;
        int count = 0;
        while(start < 63) {
            for(int j = 0; j < num; j++) {
                if(start == 63) {
                    break;
                }
                if(i == 1) {
                }
                if(encoded[start++] == '1') {
                    count += 1;
                }
            }
            for(int j = 0; j < num; j++) {
                if(start == 63) {
                    break;
                }
                start++;
            }
        }
        if(count % 2 == 0) {
            encoded[num - 1] = '0';
        } else {
            encoded[num - 1] = '1';
        }
    }


}
