# Project 1

- Link to create your repository: https://classroom.github.com/a/-slrG9xZ
- Due date: **Oct. 26, 2022** (extended)
- Final contents of repository (all in the root of your repository; no subdirectories; exact filenames):
  - `.c` and `.h` files
  - `makefile` or `Makefile` file
  - `.gitignore` (optional)
 
## Introduction
Throughout the semester, you will develop a tool with the goal of better understanding and debugging processes. The goal is to master your C and systems programming skills as you build and extend functionalities in this project. 

You must always consult the manual pages to understand the standard functions that you use in your code.

**Note:** The instructions in this project description should be portable between the current PC and Mac architectures. But their portability has not been thoroughly tested. Please discuss with me if you face any issues.

## Task
In this project, we build a program with two units: stack and stack_test.

### Function: Printing Stack (in `stack.c`)
Create a function that takes two arguments: a string message and an integer number `offset`. It should print the followings to the **standard error device**:
- "Stack (offset: offset-value): " followed by the message
- Current Stack Pointer
- Current Frame Pointer
- Contents of the `offset` number of bytes on the top of the stack presented in the form of unsigned character, unsigned int, and pointer values. Start from the `offset` and print the values at each byte interval until you reach the top of the stack.

Here is an example output of the function when `offset=32` and `message="in main"`
```
Stack (offset: 32): in main
Frame pointer: 0x00007fffffffdd80
Stack pointer: 0x00007fffffffdd60
0x00007fffffffdd80: uchar(144), uint(4294958480), pointer(0x00007fffffffdd90)
0x00007fffffffdd7f: uchar(000), uint(4292710400), pointer(0x007fffffffdd9000)
0x00007fffffffdd7e: uchar(000), uint(3717201920), pointer(0x7fffffffdd900000)
0x00007fffffffdd7d: uchar(127), uint(2415919231), pointer(0xffffffdd9000007f)
0x00007fffffffdd7c: uchar(255), uint(0000032767), pointer(0xffffdd9000007fff)
0x00007fffffffdd7b: uchar(255), uint(0008388607), pointer(0xffdd9000007fffff)
0x00007fffffffdd7a: uchar(255), uint(2147483647), pointer(0xdd9000007fffffff)
0x00007fffffffdd79: uchar(221), uint(4294967261), pointer(0x9000007fffffffdd)
0x00007fffffffdd78: uchar(128), uint(4294958464), pointer(0x00007fffffffdd80)
0x00007fffffffdd77: uchar(000), uint(4292706304), pointer(0x007fffffffdd8000)
0x00007fffffffdd76: uchar(000), uint(3716153344), pointer(0x7fffffffdd800000)
0x00007fffffffdd75: uchar(127), uint(2147483775), pointer(0xffffffdd8000007f)
0x00007fffffffdd74: uchar(255), uint(0000032767), pointer(0xffffdd8000007fff)
0x00007fffffffdd73: uchar(255), uint(0008388607), pointer(0xffdd8000007fffff)
0x00007fffffffdd72: uchar(255), uint(2147483647), pointer(0xdd8000007fffffff)
0x00007fffffffdd71: uchar(221), uint(4294967261), pointer(0x8000007fffffffdd)
0x00007fffffffdd70: uchar(112), uint(4294958448), pointer(0x00007fffffffdd70)
0x00007fffffffdd6f: uchar(000), uint(4292701952), pointer(0x007fffffffdd6f00)
0x00007fffffffdd6e: uchar(000), uint(3714973696), pointer(0x7fffffffdd6e0000)
0x00007fffffffdd6d: uchar(085), uint(1828716629), pointer(0xffffffdd6d000055)
0x00007fffffffdd6c: uchar(085), uint(0000021845), pointer(0xffffdd6c00005555)
0x00007fffffffdd6b: uchar(085), uint(0005592405), pointer(0xffdd6b0000555555)
0x00007fffffffdd6a: uchar(085), uint(1431655765), pointer(0xdd6a000055555555)
0x00007fffffffdd69: uchar(096), uint(1431655776), pointer(0x6900005555555560)
0x00007fffffffdd68: uchar(249), uint(1431658745), pointer(0x00005555555560f9)
0x00007fffffffdd67: uchar(000), uint(1432418560), pointer(0x005555555560f900)
0x00007fffffffdd66: uchar(000), uint(1626931200), pointer(0x5555555560f90000)
0x00007fffffffdd65: uchar(000), uint(4177526784), pointer(0x55555560f9000000)
0x00007fffffffdd64: uchar(000), uint(0000000000), pointer(0x555560f900000000)
0x00007fffffffdd63: uchar(000), uint(0000000000), pointer(0x5560f90000000000)
0x00007fffffffdd62: uchar(000), uint(0000000000), pointer(0x60f9000000000000)
0x00007fffffffdd61: uchar(000), uint(0000000000), pointer(0xf900000000000000)
0x00007fffffffdd60: uchar(032), uint(0000000032), pointer(0x0000000000000020)
```

You can define the following global variable to capture the stack pointer (address of the top of the stack). Depending on your architecture one of the following definitions should work:
```
register void *sp asm ("sp"); // Use one of these definitions
register void *sp asm ("esp");
register void *sp asm ("rsp");
```
You can assign `sp` to other `void *` variables or cast it to more specific types of pointer.

> Note: Some students have noticed that if you enable code optimization in `gcc` (using option `-O`; by default optimizations are disabled), the compiled code may use the stack in the middle of the `print_stack()` function (which is typically not expected). Either avoid code optimization, or copy the golbal var `sp` into a local variable in your `print_stack()` function to avoid interaction with a changing stack pointer inside the function. 

For retrieving the current frame pointer, use the following `gcc` built-in function. This should work as long as you use `gcc` as your compiler.
```
__builtin_frame_address(0);
```
Check the [function documentation](https://gcc.gnu.org/onlinedocs/gcc/Return-Address.html) for more information.

### Main and test functions (in `stack_test.c`)
In this unit, you will create 3 functions:
- `main()`: Call `print_stack()` and then call `f1(10)`.
- `f1(unsigned int)`: Call `print_stack()`. Define a local string containing "ABCD". Call  `f2()` passing the string variable as its parameter.
- `f2(char *)`: Call `print_stack()`.

Furthermore, define a macro `OFFSET=32` in this unit and use that macro in place of the offset parameter whenever you call `print_stack()`. This helps you test your code using different offsets without having to change it throughout your code.

### makefile
Create a makefile to make it easier to build the project.
Your makefile must have the following targets at the least: (you can have more targets if desired)
- `stack_test`: build the executable stack_test (this is your default target)
- `stack_test.o`: Compile the corresponding unit
- `stack.o`: Compile the corresponding unit
- `clean`: delete all object/executable files

Running `make` in your project folder must build the executable `stack_test`.

## Coding Requirements
1. Properly indent your code. There are many [indentation styles](https://en.wikipedia.org/wiki/Indentation_style). Make sure that you are consistent, using the same indentation style throughout your code.
2. Choose proper names for your variables as well as your implementation of the above functions.
3. Use inline comments to document your code. Your comments should indicate why the code is doing something, not what it is doing. For example:
  ```
  // The following comment is a "what comment" - avoid these.
  i++; // Increment i
  
  // This comment is a "why comment", which helps the reader who understands C to follow your code
  i++; // Skip over the first input item since it will always be 0
 ```
4. Clearly document your function definitions. The following is a best practice example that we suggest. At the minimum, your comment must document the job of your function, the input parameters, and the return value. For example:
  ```
  /*
  * Checks whether a file descriptor is valid
  * @param fd: file descriptor number to check
  * @param 2nd-param: (This is how you document multiple input parameters. Obviously not in the case of this function.)
  * @return 1 if tile is movable, and 0 if not.
  */
  int is_fd_valid(int fd);
  ```
  
  > **Hint:** The comment block can be right before the function definition, or at the beginning of the function definition block.
4. Check the grading rubric below which further clarifies the requirements.

## Grading Rubric
Review this tentative rubric to make sure that you have not missed instructions.

| Rubric                                                                                    | Poor                          | OK                                                                   | Good                                       | Great                                                                                  |
|-------------------------------------------------------------------------------------------|-------------------------------|----------------------------------------------------------------------|--------------------------------------------|----------------------------------------------------------------------------------------|
| Code indentation                                                                          | (0) No indentation            | (3) Existing but very inconsistent indentation                       |                                            | (5) Correct indentation throughout                                                     |
| Comments                                                                                  | (0) None/Excessive            | (2) "what", not "why" comments, few                                  | (4) Some "what" comments or missing some   | (5) Anything not obvious has reasoning                                                 |
| Function Documentation                                                                    | (0) None                      | (2) Few and/or missing parameter/return                              | (4) Most exist with most pieces in place   | (5) All functions have clear description, parameters and return values clearly defined |
| Variable/Function Naming                                                                  | (0) Single letters everywhere | (2) Lots of abbreviations                                            | (4) Full words most of the time            | (5) Full words, descriptive                                                            |
| print_stack(): print to standard error                                                    | (0) Print to standard output  |                                                                      |                                            | (5) Print to standard error                                                            |
| print_stack(): print message, offset, frame pointer, stack pointer                        | (0) None                      | (4) One/Two of the four                                              | (8) Three of the four                      | (12) All                                                                               |
| print_stack(): correct range of printed addresses: beginning/end and each byte in between | (0) None                      | (3) One of the three                                                 | (6) Two of the three                       | (9) All                                                                                |
| print_stack(): print stack contents as uchar/uint/pointer                                 | (0) None                      | (5) One of the three                                                 | (10) Two of the three                      | (17) All                                                                               |
| print_stack(): print stack contents as unsigned values with proper 0 padding              | (0) Neither                   | (4) One of the two                                                   |                                            | (8) Both                                                                               |
| Two independently compiling units: stack & stack_test                                     | (0) Single .c file            | (5) Coded in two .c files, but included one in another (single unit) |                                            | (10) Two units                                                                         |
| stack_test.c: function vars, f1()/f2() call, print_stack() calls                          | (0) Not as instructed         | (3) One of the three implemented correctly                           | (6) Two of the three implemented correctly | (9) Complete implementation                                                            |
| makefile                                                                                  | None (0)                      | Exists but missing targets (3)                                       | Exists with all targets (7)                | (10) Exists and works correctly                                                        |


