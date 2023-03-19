# Project 2

- Link to create your repository: https://classroom.github.com/a/IKqqI4Vh
- Due date: **Nov. 18, 2022 (extended)**
- Final contents of repository (all in the root of your repository; no subdirectories; exact filenames):
  - `.c` and `.h` files
  - `makefile` or `Makefile` file
  - `.gitignore` (optional)
 
## Introduction
In this project, you will develop extended functionalities on tracing functions and stack contents. You will build on what you learned in the first project. But this is an independent project and you will not directly use your previous code, although you can reuse any pieces of *your code* from the previous project.

> **Note:** You must always consult the manual pages to understand the standard functions that you use in your code. If you start using a function by relying on your understanding based on just a sample code, you will end up wasting a lot of time trying to debug the resulting errors as you may miss important information.

> **Note:** This project has been designed based on `gcc`'s capability for code instrumentation. Other compilers might not support the required options. Make sure you use `gcc`.

> **Note:** The instructions in this project description have been tested on x86 PC and Mac architectures. Start early and discuss with me if you face any issues.

## Function Call Stack Frame
This is a background information on what stack frames are. In the previous project, you got familiarized with stack contents. When a function is called, the program pushes on stack the information related to that call including parameters passed to the function, local variables, and information needed to return to the next instruction of the calling function. This data is called the call stack frame. One of the major goals of this project is to store and analyze the call stack frames of functions. 

It is very tricky to accurately determine the boundary (beginning and ending points) of a call stack frame since this information is not directly provided by the compiler. Therefore, we will use approximate boundaries in this project. Here are the items that will be in the stack frame of a function call in the order that are pushed on the stack by `gcc`:
1. Function parameters one by one, starting with the last parameter
2. Return address: This is the instruction in the calling function to which we need to return after the end of the current function.
3. Previous frame pointer: This is a backup of the frame pointer of the calling function that will be restored into the corresponding register before returning to the calling function. This pointer is **stored at the current frame pointer** address.
4. Local function variables pushed one by one.

The current function's frame pointer is stored in the base pointer register to help the compiled code access the abovementioned data in the function's stack frame. For example, assuming that each pointer is 8 bytes, the first parameter of the function will be accessible at address `frame-pointer + 2 * 8`.

## Tasks
The major task in this project is to build a code instrumentation unit that will help us analyze functions in any program.
- `inst.c` will be the main source file of your instrumentation unit
- `test.c` (included) is a test unit that has a `main()` function and few function calls for testing your instrumentation unit. You can of course use your unit to analyze any other source code!
- `stackviz.c` will be a small program to "visualize" (in the text mode) the data that you collect during program instrumentation.

### Code Instrumentation (`inst.c`)
> **Note:** You need special compilation process for this project. See the compilation section.

We will rely on `gcc`'s option `-finstrument-functions` (search for this option in the [documentation here](https://gcc.gnu.org/onlinedocs/gcc-8.1.0/gcc/Instrumentation-Options.html)) to execute code to collect data at the start and end of invoking any function in a program. This is also called code profiling. For this, you need to implement the following functions in this unit:
```
void __cyg_profile_func_enter(void *this_fn, void *call_site);
void __cyg_profile_func_exit(void *this_fn, void *call_site);
```
When we compile our programs with the above-mentioned option, function `__cyg_profile_func_enter()` will be called at the beginning of all functions in our program and function `__cyg_profile_func_exit()` will be called at the end of those functions. Parameters `this_fn` and `call_site` indicate pointers to the code segment that refer to the profiled function and the instruction that has invoked the profiled function. 

We can implement various functionalities using these enter/exit functions to help us analyze the function calls in our program. In this project, you will collect information about stack frames and simple execution time of each function. 

#### Collecting Call Information
In this project, you need to collect the following information for each call. It is recommended that you maintain a linked list of structures to hold and update the information for each function call. You may choose to store additional information as needed too.
- `this_fn` and `call_site`
- Frame pointer: Use `__builtin_frame_address(1)` to retrieve the profiled function's frame pointer. Note that `__builtin_frame_address(0)` will return the frame pointer of the enter/exit function themselves. We need to pass 1 to retrieve the corresponding value for the caller of our enter/exit function, which is the actual profiled function.
- Beginning address of the stack frame: For the beginning address, consider including both the previous frame pointer and return address:
  ```
  // beginning address = frame pointer of profiled function + 2 * sizeof(void *);
  ```
  In other words, we do not include the function parameters since it is complicated to calculate their exact boundary.
- Ending address of the stack frame: The stack frame of the profiled function will be before the stack frame of the current enter/exit function. To estimate the ending point, we can use the current frame pointer (use `__builtin_frame_address(0)`) and consider space for the previous frame pointer, the return address, and the two pointer argument of the enter/exit function. This is again a rough estimate:
  ```
  // ending address = frame pointer of enter/exit function + 4 * sizeof(void *);
  ```
- Initial contents of the stack frame (at enter time)
- Final contents of the stack frame (at exit time)
- Start and finish clock values as well as running time of the function: You can use `clock()` (see `man 3 clock`) to retrieve the values. You can calculate the running time of the function:
  ```
  running_time = (double) (finish - start) / CLOCKS_PER_SEC;
  ```
  
> **Hint:** You need dynamic allocation for the data structure holding the stack frames. You also need to dynamically allocate for the two copies of the stack frame contents (initial and final contents).
  
#### Storing Call Information
At the end of the program, i.e., when the very first function (`main()`) ends, your instrumentation unit needs to store the call information in a binary file called `stack.bin`. Note that in addition to the metadata related to the function call, you need to store the actual contents of initial/final stack frame.

### Visualizing Stack Frames (`stackviz.c`)
Write a separate program that takes one command line argument indicating a binary stack file (e.g., `stack.bin`). Provide an appropriate error message in case of a missing filename or error opening it.

The program should visualize the stack frame and call information in the file as shown in the following sample output: 
```
function: 0x5639db7fc2ca, caller: 0x7f8b8f372290, frame pointer: 0x7ffd23831070
stack frame: 0x7ffd23831080-0x7ffd23831070, time: 0.003484 (2158-5642)
address range                  initial    final
0x7ffd2383107f-0x7ffd2383107c: 00007f8b | 00007f8b
0x7ffd2383107b-0x7ffd23831078: 8f372290 | 8f372290
0x7ffd23831077-0x7ffd23831074: 00000000 | 00000000
0x7ffd23831073-0x7ffd23831070: 00000001 | 00000001

function: 0x5639db7fc1d9, caller: 0x5639db7fc2f3, frame pointer: 0x7ffd23831050
stack frame: 0x7ffd23831060-0x7ffd23831030, time: 0.002587 (2173-4760)
address range                  initial    final
0x7ffd2383105f-0x7ffd2383105c: 00005639 | 00005639
0x7ffd2383105b-0x7ffd23831058: db7fc2f3 | db7fc2f3
0x7ffd23831057-0x7ffd23831054: 00007ffd | 00007ffd
0x7ffd23831053-0x7ffd23831050: 23831070 | 23831070
0x7ffd2383104f-0x7ffd2383104c: bbc0f880 | bbc0f880
0x7ffd2383104b-0x7ffd23831048: 778a2800 | 778a2800
0x7ffd23831047-0x7ffd23831044: 00000000 | 03030303
0x7ffd23831043-0x7ffd23831040: 00000000 | 03030303
0x7ffd2383103f-0x7ffd2383103c: 00005639 | 03035639
0x7ffd2383103b-0x7ffd23831038: db7fc2ca | 89abcdef
0x7ffd23831037-0x7ffd23831034: 00007f8b | 0000000a
0x7ffd23831033-0x7ffd23831030: 8f372290 | 000186a0

function: 0x5639db7fc1d9, caller: 0x5639db7fc29d, frame pointer: 0x7ffd23831010
stack frame: 0x7ffd23831020-0x7ffd23830ff0, time: 0.001733 (3015-4748)
address range                  initial    final
0x7ffd2383101f-0x7ffd2383101c: 00005639 | 00005639
0x7ffd2383101b-0x7ffd23831018: db7fc29d | db7fc29d
0x7ffd23831017-0x7ffd23831014: 00007ffd | 00007ffd
0x7ffd23831013-0x7ffd23831010: 23831050 | 23831050
0x7ffd2383100f-0x7ffd2383100c: bbc0f880 | bbc0f880
0x7ffd2383100b-0x7ffd23831008: 778a2800 | 778a2800
0x7ffd23831007-0x7ffd23831004: 00007ffd | 02020202
0x7ffd23831003-0x7ffd23831000: 23831050 | 02020202
0x7ffd23830fff-0x7ffd23830ffc: 00007ffd | 02027ffd
0x7ffd23830ffb-0x7ffd23830ff8: 23831188 | 89abcdef
0x7ffd23830ff7-0x7ffd23830ff4: 00005639 | 0000000a
0x7ffd23830ff3-0x7ffd23830ff0: db7fc2f3 | 000186a0

function: 0x5639db7fc1d9, caller: 0x5639db7fc29d, frame pointer: 0x7ffd23830fd0
stack frame: 0x7ffd23830fe0-0x7ffd23830fb0, time: 0.000851 (3883-4734)
address range                  initial    final
0x7ffd23830fdf-0x7ffd23830fdc: 00005639 | 00005639
0x7ffd23830fdb-0x7ffd23830fd8: db7fc29d | db7fc29d
0x7ffd23830fd7-0x7ffd23830fd4: 00007ffd | 00007ffd
0x7ffd23830fd3-0x7ffd23830fd0: 23831010 | 23831010
0x7ffd23830fcf-0x7ffd23830fcc: bbc0f880 | bbc0f880
0x7ffd23830fcb-0x7ffd23830fc8: 778a2800 | 778a2800
0x7ffd23830fc7-0x7ffd23830fc4: 00007ffd | 01010101
0x7ffd23830fc3-0x7ffd23830fc0: 23831010 | 01010101
0x7ffd23830fbf-0x7ffd23830fbc: 00007ffd | 01017ffd
0x7ffd23830fbb-0x7ffd23830fb8: 23831188 | 89abcdef
0x7ffd23830fb7-0x7ffd23830fb4: 00005639 | 0000000a
0x7ffd23830fb3-0x7ffd23830fb0: db7fc29d | 000186a0

function: 0x5639db7fc1d9, caller: 0x5639db7fc2fd, frame pointer: 0x7ffd23831050
stack frame: 0x7ffd23831060-0x7ffd23831030, time: 0.000851 (4781-5632)
address range                  initial    final
0x7ffd2383105f-0x7ffd2383105c: 00005639 | 00005639
0x7ffd2383105b-0x7ffd23831058: db7fc2fd | db7fc2fd
0x7ffd23831057-0x7ffd23831054: 00007ffd | 00007ffd
0x7ffd23831053-0x7ffd23831050: 23831070 | 23831070
0x7ffd2383104f-0x7ffd2383104c: bbc0f880 | bbc0f880
0x7ffd2383104b-0x7ffd23831048: 778a2800 | 778a2800
0x7ffd23831047-0x7ffd23831044: 03030303 | 01010101
0x7ffd23831043-0x7ffd23831040: 03030303 | 01010101
0x7ffd2383103f-0x7ffd2383103c: 03035639 | 01015639
0x7ffd2383103b-0x7ffd23831038: 89abcdef | 89abcdef
0x7ffd23831037-0x7ffd23831034: 0000000a | 0000000a
0x7ffd23831033-0x7ffd23831030: 000186a0 | 000186a0
```

As shown above, there are 5 function calls in the provided `test.c`. The printout for each function call include the function address, call site, frame pointer, stack frame range, and execution time (including start/finish clock values).
The contents of the stack frame are then displayed four bytes at a time, where the initial contents and final contents of the corresponding addresses are shown side-by-side. This will help us see what part of the stack gets modified in the course of a function call.

### Compilation and makefile
> **Warning:** Missing the following compilation instructions can result in segmentation faults. Essentially, we need to avoid instrumenting our instrumentation code! Otherwise, it will turn it to a recursive function call that will eventually crash our program. 

It is critical to compile the instrumentation code separately in this project, and then compile/link it together with the source of your target program (here, `test.c`). In order to generate an instrumented executable for the `test.c` program, you need to do the followings:
```
gcc -c inst.c
gcc -finstrument-functions test.c inst.o -o test-inst
```

Create a makefile similar to the last project to facilitate rebuilding the project and creating executables `test`, `test-inst`, and `stackviz` by just typing `make`.

> Hint: Here is a sample set of targets:
> - `all`: write it as `all: test test-inst stackviz` (This is the default target.)
> - `test`: produce `test` from `test.c` without instrumenting (regular compilation; not including `inst.c`)
> - `test-inst`: produce `test-inst` using the second `gcc` command provided in the instruction
> - `inst.o`: compile `inst.c`
> - `stackviz`: produce executable corresponding to `stackviz.c`
> - `clean`: do the usual cleanup

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
5. Check the grading rubric below which further clarifies the requirements.

## Grading Rubric
Review this tentative rubric to make sure that you have not missed instructions.

| Rubric                                                                                 | Poor                          | OK                                           | Good                                                  | Great                                                                                  |
|----------------------------------------------------------------------------------------|-------------------------------|----------------------------------------------|-------------------------------------------------------|----------------------------------------------------------------------------------------|
| Code indentation                                                                       | (0) No indentation            | (2) Existing but inconsistent indentation    |                                                       | (3) Correct indentation throughout                                                     |
| Comments                                                                               | (0) None/Excessive            | (1) "what", not "why" comments, few          | (2) Some "what" comments or missing some              | (3) Anything not obvious has reasoning                                                 |
| Function Documentation                                                                 | (0) None                      | (1) Few and/or missing parameter/return      | (2) Most exist with most pieces in place              | (3) All functions have clear description, parameters and return values clearly defined |
| Variable/Function Naming                                                               | (0) Single letters everywhere | (1) Lots of abbreviations                    | (2) Full words most of the time                       | (3) Full words, descriptive                                                            |
| `inst.c`: stack frame structure and linked list                                        | (0) None                      | (3) Correct node structure                   | (6) Correct node structure and partially correct list | (10) All work correctly                                                                |
| `inst.c`: capturing frame pointer, begin/end address                                   | (0) None                      | (2) One of the three                         | (4) Two of the three                                  | (6) All                                                                                |
| `inst.c`: capturing initial contents (in function's enter)                             | (0) No                        | (2) Allocated, but not copied correctly      |                                                       | (5) Allocated and copied correctly                                                     |
| `inst.c`: capturing final contents (in function's exit)                                | (0) No                        | (2) Allocated, but not copied correctly      |                                                       | (5) Allocated and copied correctly                                                     |
| `inst.c`: capturing time (in function's enter/exit)                                    | (0) No                        | (2) Only start time                          | (4) both start/end time, but no running time          | (5) All correctly                                                                      |
| `inst.c`: storing stack frames in order of being called in stack.bin                   | (0) No                        | (2) Partially                                | (4) All, but not in correct order                     | (5) All and in correct order                                                           |
| `inst.c`: storing initial/final contents of stack frames in stack.bin                  | (0) No                        | (5) One of the two                           |                                                       | (10) Both                                                                              |
| `stackviz.c`: allocating for and retrieving stack frames and contents                  | (0) No                        | (5) Only basic frame info.                   | (7) Basic info. + only one of initial/final contents  | (10) All imported correctly                                                            |
| `stackviz.c`: print function, caller, and frame pointer                                | (0) None                      | (2) One of the three                         | (4) Two of the three                                  | (6) All                                                                                |
| `stackviz.c`: print correct stack frame range and running time                         | (0) None                      | (2) Partially correct range and time         | (4) One fully and another partially correct           | (6) All done correctly                                                                 |
| `stackviz.c`: print correctly line-by-line contents: address range, initial, and final | (0) None                      | (3) One of the three                         | (6) Two of the three                                  | (10) All                                                                               |
| `stackviz.c`: print line-by-line contents according to provided format                 | (0) None                      | (2) Major formatting issues                  | (4) Minor formatting issues                           | (5) Perfectly                                                                          |
| Provided two-line compilation works as expected, producing instrumented `test-inst`    | (0) No                        |                                              |                                                       | (10) Yes                                                                               |
| `makefile` for producing executables `test`, `test-inst`, and `stackviz`               | (0) None                      | (2) Only produces one of the three correctly | (4) Only produces two of the three correctly          | (5) Works correctly                                                                    |
