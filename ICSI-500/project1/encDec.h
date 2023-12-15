
//Physical Layer
char* charToBits(char ch);
char* bitsToChar(char* bits);
char* addParity(char*bits);
char* validateRemoveParity(char* bits);

//Data Link Layer
char* toFrame(char syn1, char syn2, int length, char* data);
char* fromFrame(char* data);
char* errorSimuation(char* data);

//Application Layer
char* readInput(char* name);
char* outputToFile(char* stream);
char toUpperCase(char ch);
