int main(int argc, char *argv[]) {
    unsigned int number = strtol(argv[1], (char **)NULL, 10);
    printf("Your number was %i\n", number);
    int zeroCounter = 0;
    int oneCounter = 0;
    while (number > 0) {
        if(number % 2 == 1) {
            oneCounter += 1;
        }
        number = number / 2;
    }
    printf("One Count: %i\n", oneCounter);
    printf("Zero Count: %i", 32 - oneCounter);

    return 0;
}