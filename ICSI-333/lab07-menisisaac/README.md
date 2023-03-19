# Lab 07: POSIX Files

- Link to create your repository: https://classroom.github.com/a/LS4GSpRx
- Due date: Oct. 28, 2022
- Final contents of repository (all in the root of your repository; no subdirectories; exact filenames):
  - `pfiles1.c`
  - `pfiles2.c`
  - `numbers_XXXX.txt`
  - `.gitignore` (optional)

---

## Reading Materials and Resources
- Lecture slides and readings
- `inclass-syscall` repository

## Task 1: Creating Output Files
Write a program (`pfiles1.c`) that:

1. Creates an array of 100 random integers in the range of 0-99. The program has no input data (it should use standard functions to generate sequence of random numbers).
2. The program sums the random numbers and prints the sum.
3. It then writes the numbers to a new file using `open()`, `close()` and `write()`. The filename should be: `numbers_XXXX.txt` where XXXX is the sum of the numbers in the file.
4. The program must contain only one function: `int main(void)`.
5. Commit your `numbers_XXXX.txt` in addition to your source code.

> **Hints**:
> - Remember to initialize any sum variable to 0`
> - `rand()` will return a random number too big for 0-99. Use modulo to reduce the range.
> - `rand()` needs to be initialized with a seed value. You can use: `srand(time(NULL));`

## Task 2: Finding and Processing Input Files
Write a program (`pfiles2.c`) that:

1. Looks in the current directory for files that match the pattern `numbers_XXXX.txt`.
2. For each file, open the file and read the file. You can assume that the file will contain 100 integers.
3. Recalculate the sum of the integers.
4. Print the filename and the recalculated sum of the integers.

> **Hints**:
> - `opendir()` requires a starting directory.
> - You want to start in the current directory.
> - You need to loop over the directory entries.
> - This is similar to walking a linked list.
> - You want to check every file entry to see if it starts with `numbers_`.
> - Make a function to deal with open/read/sum/print/close for a given file.

## Rubric
- Task 1
  - 1 point : Create random numbers
  - 1 point : Print the sum of the numbers
  - 1 point : Create the file using `open()`
  - 1 point : Write the file using `write()`
  - 1 point : Close the file using `close()`

- Task 2
  - 2 points: Search the directory using directory methods
  - 2 points: Use `open()`/`read()`/`close()` to read the file
  - 1 point : Sum and print the results
