# Lab 06: Files

- Link to create your repository: https://classroom.github.com/a/NcaPmN3d
- Due date: Oct. 21, 2022
- Final contents of repository (all in the root of your repository; no subdirectories; exact filenames):
  - `files.c`
  - `.gitignore` (optional)

---

## Reading Materials and Resources
- Lecture slides and readings

## Task 1: Learn about files
Recall file handling and some functions of basic file operations:

| Function  | Description                                   |
| --------- | --------------------------------------------- |
| fopen()   | create a new file or open an existing file    |
| fclose()  | closes a file                                 |
| fscanf()  | reads a set of data from a file using a formatted string (textual)|
| fprintf() | writes a set of data to a file using a formatted string (textual) |
| fread()   | reads a set of data from a file (binary)      |
| fwrite()  | writes a set of data to a file (binary)       |
| getc()    | reads a character from a file                 |
| putc()    | writes a character to a file                  |
| getw()    | reads an integer from a file                  |
| putw()    | writes an integer to a file                   |
| getline() | read a line from a file                       |
| ftell()   | gives current position in the file            |
| fseek()   | set the position to desire point              |
| rewind()  | set the position to the beginning of the file |

File handling steps:

1. Declare a file pointer variable, `FILE *fp;`
2. Open a file using `fopen()` function
3. Process the file using suitable functions.
4. Close the file using `fclose()` function.

This is a good time to remind you about Linux command `man` (short for manual). It shows you the built-in documentation on any command or function in the system. Sometimes the same command/function name exists in more than one section of the manual. If so, `man` by default shows you the first occurance. You can ask for showing the manual page in a specific section by including section number before the page you are looking for. The C standard functions are in Section 3 of the manual.

Try:

```
man fopen
man exit # By default this shows the shell "exit" command
man 3 exit # This shows C "exit()" function
```

## Task 2: Your assignment

1. Write a program that reads a file `input.txt` line by line.
2. Print every word of a line on screen, every word on a new line (any sequence delimited by one or more whitespace characters is considered as a word). In addition to the space character (ASCII code 32), you can also consider another common whitespace character, tab (ASCII code 9).
3. Commit your `files.c` file.

### Example Run
Assume the following is the contents of `input.txt`:
```
Here is    an example input file!
Any sequence of non-space characters such as XYZ123- should 
be interpreted as one word.
```
Your program should print the following lines:
```
Here
is
an
example
input
file!
Any
sequence
of
non-space
characters
such
as
XYZ123-
should 
be
interpreted
as
one
word.
```

## Grading Rubric
- 2 points: Open the file
- 2 points: Read the file line by line
- 3 points: Split each line by spaces
- 1 point : Print the resultant words
- 2 points: Close the file
