#include <stdio.h>
#include <stdlib.h>
#include <time.h>

struct intNode {
    int data;
    struct intNode *next;
};

int main() {
    struct intNode *head = malloc(sizeof(struct intNode));
    head->data = rand() % 36;
    head->next = NULL;
    struct intNode *current = head;
    srand(time(NULL));
    while(current ->data != 35) {
        current -> next = malloc(sizeof(struct intNode));
        current = current->next;
        int temp = rand() % 36;
        if(temp == 35) {
            current->next = NULL;
            break;
        }
        current -> data = temp;


    }
    current = head;
    struct intNode *temp;
    while(current != NULL) {
        printf("%d\n", current->data);
        temp = current;
        current = current->next;
        free(temp);
    }

    return 0;
}
