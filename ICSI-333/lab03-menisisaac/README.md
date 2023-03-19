# Lab 03: Bitwise Operators
- Link to create your repository: https://classroom.github.com/a/tkZNv8Pu
- Due date: **Sep. 19, 2022 (extended)**
- Final contents of repository (all in the root of your repository; no subdirectories; exact filenames):
  - `bitwise.c`
  - `.gitignore` (optional)

---
## Reading Materials and Resources
- Lecture slides.
- See `cmdline.c` in `inclass-c` repo for an example of how to work with command line arguments.

## Task 1: Learn about Bitwise Operators
The bitwise operators are very similar to the ones that you might remember from Java. They are used a lot more frequently in C, though, because C is mostly chosen when you want to write low level programs.

A quick reminder:

& (bitwise AND)
: Takes two numbers as operand and does AND on every bit of two numbers. The result of AND is 1 only if both bits are 1.

| (bitwise OR)
: Takes two numbers as operand and does OR on every bit of two numbers. The result of OR is 1 any of the two bits is 1.

^ (bitwise XOR)
: Takes two numbers as operand and does XOR on every bit of two numbers. The result of XOR is 1 if the two bits are different.

<< (left shift)
: Takes two numbers, left shifts the bits of first operand, the second operand decides the number of places to shift.

\>> (right shift)
: Takes two numbers, right shifts the bits of first operand, the second operand decides the number of places to shift.

~ (bitwise NOT)
: Takes one number and inverts all bits of it. 

Remember that && and || are different from & and |.


## Task 2: Your assignment

1. Write a program (`bitwise.c`) that takes a number from the command line.
2. Print the number.
3. Print the number of bits in the number that are set to 0 and 1, separately.
4. Commit your `.c` file.

> **Hint**: use an `unsigned int` or `int` variable to store your number. It will be almost certainly 4 bytes or 32 bits in your machine. But if you want to have a more robust code, you can use `sizeof(your-variable) * 8` for the number of bits.

For example:

```
./bitwise 48
Your number was 48
In 48, there are 30 bits set to 0.
In 48, there are 2 bits set to 1.
```
