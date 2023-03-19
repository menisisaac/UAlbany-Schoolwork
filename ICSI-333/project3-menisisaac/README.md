# Project 3
- Link to create your repository: https://classroom.github.com/a/EE8aHCEp
- Due Date: **Dec. 5, 2022 (No Extension)**
- Final contents of repository (all in the root of your repository; no subdirectories; exact filenames):
  - `.c` and `.h` files
  - `makefile` or `Makefile` file
  - `.gitignore` (optional)
 
> **Note:** There will be some updates to the instructions by Thursday, Nov. 17th. Be sure to check the instructions again.

## Introduction
Building on the previous project, in this project, you will develop stack tracking and visualization functionality to programs that may create multiple processes.

You can reuse **your code** from the previous project. But note that you will need to update parts of it to handle the work of this project.

> **Note:** You must always consult the manual pages to understand the standard functions that you use in your code. If you start using a function by relying on your understanding based on just a sample code, you will end up wasting a lot of time trying to debug the resulting errors as you may miss important information.

> **Note:** This project has been designed based on `gcc`'s capability for code instrumentation. Other compilers might not support the required options. Make sure you use `gcc`.

## Tasks
Your code in this project will be in the same modular structure as in the last project. However, two separate test programs are provided.
- `inst.c` will be the main source file of your instrumentation unit.
- `testpipe.c` and `testfork.c` are two separate test programs that have `main()` functions. Your instrumentation must work for both programs (or any other multi-process program).
- `stackviz.c` will be the program to "visualize" (in the text mode) the data that you collect during program instrumentation.

### Code Instrumentation (`inst.c`)
#### Parent and Child Process Call Tracking
Recall that when a process forks a child process the child will be a duplicate of the parent right after the fork. Therefore, the child will have its own stack and other segments. Your instrumentation should store a stack frame data structure corresponding to each function call exit.

Suppose process `P1` forks process `P2` in the middle of function `f()` that was called from `main()`. Assuming the child process finishes before the parent process, your instrumentation should store four stack frames in the file at their exit points:
- `f()` of `P2`
- `main()` of `P2`
- `f()` of `P1`
- `main()` of `P1`

You will need to open the binary file at the first enter and close it at the last exit. In between, every exit function call will need to store its stack frame and content data in the file. Note that you will only need to open the binary file in the first (main) process. Every forked process will inherit the open file descriptor and can use that for writing the data.

#### Preventing Race Condition
As in last project, storing stack frame involves storing stack frame structure as well as the dynamically allocated memory contents of initial and final. You need to ensure that your writing to the file (which may involve multiple writes) is atomic every time. Otherwise, different processes may write their data interleaved in the binary file, corrupting your expected format of the binary file. In other words, you will have a race condition! You should use a mechanism such as **named semaphores** to prevent such problems.

> Hint: `testfork-inst` would be more likely to create race condition situations if you do not implement a way to ensure the atomicity of writing the stack frame data for each call.

#### Collecting Call Information
The instrumentation data collection in this project has a few updates and additions compared to the last project:
- Store the **process ID** and the **parent process ID** corresponding to a function call (using `getpid()` and `getppid()`). Store both of these values separately at both the start of the function (enter) and at the finish of the function (exit). Note that a function call can start in one process, but end in another process. See the sample output.
- Since function `clock()` is only useful within one process you need to use a different method for tracking start/finish time. Use function `clock_gettime()` instead. The sample code below shows how you can store the result. 
    ```
    struct timespec start_time;
    clock_gettime(CLOCK_MONOTONIC, &start_time); 

    ```
    > Hint: You should use elements of type `struct timespec` for times in your stack frame data structure.
  
#### Storing Call Information
Similar to the last project, the stack frame data must be stored in `stack.bin`.

> Hint: Make sure you truncate the output file. Do not simply overwrite it running the risk of leaving data behind from the previous runs.

The previous project instructions had suggested storing stack frame data at the end of the program. In this project, you should store the stack frame of a function call as soon as the function call ends (at its exit point). This will result in storing stack frames in the order of their finish time.

Similar to the last project, note that in addition to the metadata related to the function call, you need to store the actual contents of initial/final stack frame.

### Visualizing Stack Frames (`stackviz.c`)
Your stack visualization program works similarly as before, but with a few updated functionalities.

#### Updated Command Syntax
The new version of the program should allow options to modify the visualization (3 options). The new syntax of the command should be as follows.
```
stackviz [OPTION] FILENAME

List of options:
-s, --order-by-start-time: Order stack frames by the start time of the calls. 
-f, --order-by-finish-time: Order stack frames by the finish time of the calls. The ordering should be similar to the case that no option is provided.
-p, --order-by-finish-pid-start-time: Order stack frames by the process ID at the call finish and then the start time of the calls.
```

#### Updated Printouts
Print an index (counter) starting from 1 before printing each stack frame.

At the beginning of the printout for each stack frame, print the process ID and parent process ID of the corresponding process at the start and at the end of the function call. 

For printing `timespec` data structure, you can use something like the following. The structure has two elements corresponding to seconds and nanoseconds. There is no need to print the execution time (difference between the start and finish time) in this project, even though the sample outputs show that.
```
printf("%ld.%.9ld", start_time.tv_sec, start_time.tv_nsec);

```

#### Sample Stackviz Outputs
Below is a sample (unsorted) output of `stackviz` for the binary data generated by `testpipe-inst` program.
```
(1)
start process: 692787, start parent process: 692786
finish process: 692787, finish parent process: 692786
function: 0x55f1e532c3d8, caller: 0x55f1e532c509, frame pointer: 0x7ffc6da28ad0
stack frame: 0x7ffc6da28ae0-0x7ffc6da28ac0, time: 9.000517535 (249983.882388591-249992.882906126)
address range                  initial    final
0x7ffc6da28adf-0x7ffc6da28adc: 000055f1 | 000055f1
0x7ffc6da28adb-0x7ffc6da28ad8: e532c509 | e532c509
0x7ffc6da28ad7-0x7ffc6da28ad4: 00007ffc | 00007ffc
0x7ffc6da28ad3-0x7ffc6da28ad0: 6da28b00 | 6da28b00
0x7ffc6da28acf-0x7ffc6da28acc: 00007ffc | 00000003
0x7ffc6da28acb-0x7ffc6da28ac8: 6da28c28 | 6da28c28
0x7ffc6da28ac7-0x7ffc6da28ac4: 00000000 | 00000000
0x7ffc6da28ac3-0x7ffc6da28ac0: 00000000 | 00000000

(2)
start process: 692786, start parent process: 592583
finish process: 692787, finish parent process: 692786
function: 0x55f1e532c47d, caller: 0x7fc5f85ef290, frame pointer: 0x7ffc6da28b00
stack frame: 0x7ffc6da28b10-0x7ffc6da28af0, time: 9.001274044 (249983.881875661-249992.883149705)
address range                  initial    final
0x7ffc6da28b0f-0x7ffc6da28b0c: 00007fc5 | 00007fc5
0x7ffc6da28b0b-0x7ffc6da28b08: f85ef290 | f85ef290
0x7ffc6da28b07-0x7ffc6da28b04: 00000000 | 00000000
0x7ffc6da28b03-0x7ffc6da28b00: 00000001 | 00000001
0x7ffc6da28aff-0x7ffc6da28afc: 00007ffc | 00007ffc
0x7ffc6da28afb-0x7ffc6da28af8: 6da28c18 | 6da28c18
0x7ffc6da28af7-0x7ffc6da28af4: 00000000 | 00000000
0x7ffc6da28af3-0x7ffc6da28af0: 00000000 | 00000000

(3)
start process: 692786, start parent process: 592583
finish process: 692786, finish parent process: 592583
function: 0x55f1e532c2b9, caller: 0x55f1e532c51d, frame pointer: 0x7ffc6da28ad0
stack frame: 0x7ffc6da28ae0-0x7ffc6da28aa0, time: 9.001720771 (249983.882204435-249992.883925206)
address range                  initial    final
0x7ffc6da28adf-0x7ffc6da28adc: 000055f1 | 000055f1
0x7ffc6da28adb-0x7ffc6da28ad8: e532c51d | e532c51d
0x7ffc6da28ad7-0x7ffc6da28ad4: 00007ffc | 00007ffc
0x7ffc6da28ad3-0x7ffc6da28ad0: 6da28b00 | 6da28b00
0x7ffc6da28acf-0x7ffc6da28acc: 00007ffc | 00007ffc
0x7ffc6da28acb-0x7ffc6da28ac8: 6da28c18 | 6da28c18
0x7ffc6da28ac7-0x7ffc6da28ac4: 00000000 | 00000000
0x7ffc6da28ac3-0x7ffc6da28ac0: 00000000 | 00000000
0x7ffc6da28abf-0x7ffc6da28abc: d167394b | d167394b
0x7ffc6da28abb-0x7ffc6da28ab8: 49522600 | 49522600
0x7ffc6da28ab7-0x7ffc6da28ab4: 00007ffc | 00212165
0x7ffc6da28ab3-0x7ffc6da28ab0: 6da28c18 | 79628c18
0x7ffc6da28aaf-0x7ffc6da28aac: d167394b | 00000000
0x7ffc6da28aab-0x7ffc6da28aa8: 49522600 | 49522600
0x7ffc6da28aa7-0x7ffc6da28aa4: 00000000 | 00000000
0x7ffc6da28aa3-0x7ffc6da28aa0: 00000011 | 00000011

(4)
start process: 692786, start parent process: 592583
finish process: 692786, finish parent process: 592583
function: 0x55f1e532c47d, caller: 0x7fc5f85ef290, frame pointer: 0x7ffc6da28b00
stack frame: 0x7ffc6da28b10-0x7ffc6da28af0, time: 9.002699960 (249983.881875661-249992.884575621)
address range                  initial    final
0x7ffc6da28b0f-0x7ffc6da28b0c: 00007fc5 | 00007fc5
0x7ffc6da28b0b-0x7ffc6da28b08: f85ef290 | f85ef290
0x7ffc6da28b07-0x7ffc6da28b04: 00000000 | 00000000
0x7ffc6da28b03-0x7ffc6da28b00: 00000001 | 00000001
0x7ffc6da28aff-0x7ffc6da28afc: 00007ffc | 00007ffc
0x7ffc6da28afb-0x7ffc6da28af8: 6da28c18 | 6da28c18
0x7ffc6da28af7-0x7ffc6da28af4: 00000000 | 00000000
0x7ffc6da28af3-0x7ffc6da28af0: 00000000 | 00000000

```

Below is a sample (unsorted) output of `stackviz` for the binary data generated by `testfork-inst` program.
```
(1)
start process: 691397, start parent process: 691396
finish process: 691397, finish parent process: 691396
function: 0x5629986b4249, caller: 0x5629986b42de, frame pointer: 0x7ffed537bc20
stack frame: 0x7ffed537bc30-0x7ffed537bc10, time: 0.000006633 (249512.354693094-249512.354699727)
address range                  initial    final
0x7ffed537bc2f-0x7ffed537bc2c: 00005629 | 00005629
0x7ffed537bc2b-0x7ffed537bc28: 986b42de | 986b42de
0x7ffed537bc27-0x7ffed537bc24: 00007ffe | 00007ffe
0x7ffed537bc23-0x7ffed537bc20: d537bc50 | d537bc50
0x7ffed537bc1f-0x7ffed537bc1c: 00005629 | 00000000
0x7ffed537bc1b-0x7ffed537bc18: 9882b5c0 | 9882b5c0
0x7ffed537bc17-0x7ffed537bc14: 00007ffe | 00007ffe
0x7ffed537bc13-0x7ffed537bc10: d537be28 | d537be28

(2)
start process: 691398, start parent process: 691396
finish process: 691398, finish parent process: 691396
function: 0x5629986b4249, caller: 0x5629986b42de, frame pointer: 0x7ffed537bc20
stack frame: 0x7ffed537bc30-0x7ffed537bc10, time: 0.000009362 (249512.354770704-249512.354780066)
address range                  initial    final
0x7ffed537bc2f-0x7ffed537bc2c: 00005629 | 00005629
0x7ffed537bc2b-0x7ffed537bc28: 986b42de | 986b42de
0x7ffed537bc27-0x7ffed537bc24: 00007ffe | 00007ffe
0x7ffed537bc23-0x7ffed537bc20: d537bc50 | d537bc50
0x7ffed537bc1f-0x7ffed537bc1c: 00005629 | 00000000
0x7ffed537bc1b-0x7ffed537bc18: 9882b5c0 | 9882b5c0
0x7ffed537bc17-0x7ffed537bc14: 00007ffe | 00007ffe
0x7ffed537bc13-0x7ffed537bc10: d537be28 | d537be28

(3)
start process: 691398, start parent process: 691396
finish process: 691398, finish parent process: 691396
function: 0x5629986b42a1, caller: 0x5629986b4288, frame pointer: 0x7ffed537bc50
stack frame: 0x7ffed537bc60-0x7ffed537bc40, time: 0.000617040 (249512.354756592-249512.355373632)
address range                  initial    final
0x7ffed537bc5f-0x7ffed537bc5c: 00005629 | 00005629
0x7ffed537bc5b-0x7ffed537bc58: 986b4288 | 986b4288
0x7ffed537bc57-0x7ffed537bc54: 00007ffe | 00007ffe
0x7ffed537bc53-0x7ffed537bc50: d537bc80 | d537bc80
0x7ffed537bc4f-0x7ffed537bc4c: 00005629 | 00000001
0x7ffed537bc4b-0x7ffed537bc48: 9882b510 | 9882b510
0x7ffed537bc47-0x7ffed537bc44: 00007ffe | 00007ffe
0x7ffed537bc43-0x7ffed537bc40: d537be28 | d537be28

(4)
start process: 691397, start parent process: 691396
finish process: 691397, finish parent process: 691396
function: 0x5629986b42a1, caller: 0x5629986b4288, frame pointer: 0x7ffed537bc50
stack frame: 0x7ffed537bc60-0x7ffed537bc40, time: 0.000280123 (249512.354683440-249512.354963563)
address range                  initial    final
0x7ffed537bc5f-0x7ffed537bc5c: 00005629 | 00005629
0x7ffed537bc5b-0x7ffed537bc58: 986b4288 | 986b4288
0x7ffed537bc57-0x7ffed537bc54: 00007ffe | 00007ffe
0x7ffed537bc53-0x7ffed537bc50: d537bc80 | d537bc80
0x7ffed537bc4f-0x7ffed537bc4c: 00005629 | 00000001
0x7ffed537bc4b-0x7ffed537bc48: 9882b510 | 9882b510
0x7ffed537bc47-0x7ffed537bc44: 00007ffe | 00007ffe
0x7ffed537bc43-0x7ffed537bc40: d537be28 | d537be28

(5)
start process: 691398, start parent process: 691396
finish process: 691398, finish parent process: 691396
function: 0x5629986b4249, caller: 0x5629986b42de, frame pointer: 0x7ffed537bc80
stack frame: 0x7ffed537bc90-0x7ffed537bc70, time: 0.000706679 (249512.354750427-249512.355457106)
address range                  initial    final
0x7ffed537bc8f-0x7ffed537bc8c: 00005629 | 00005629
0x7ffed537bc8b-0x7ffed537bc88: 986b42de | 986b42de
0x7ffed537bc87-0x7ffed537bc84: 00007ffe | 00007ffe
0x7ffed537bc83-0x7ffed537bc80: d537bcb0 | d537bcb0
0x7ffed537bc7f-0x7ffed537bc7c: 00005629 | 00000002
0x7ffed537bc7b-0x7ffed537bc78: 9882b460 | 9882b460
0x7ffed537bc77-0x7ffed537bc74: 00007ffe | 00007ffe
0x7ffed537bc73-0x7ffed537bc70: d537be28 | d537be28

(6)
start process: 691397, start parent process: 691396
finish process: 691397, finish parent process: 691396
function: 0x5629986b4249, caller: 0x5629986b42de, frame pointer: 0x7ffed537bc80
stack frame: 0x7ffed537bc90-0x7ffed537bc70, time: 0.000875410 (249512.354675867-249512.355551277)
address range                  initial    final
0x7ffed537bc8f-0x7ffed537bc8c: 00005629 | 00005629
0x7ffed537bc8b-0x7ffed537bc88: 986b42de | 986b42de
0x7ffed537bc87-0x7ffed537bc84: 00007ffe | 00007ffe
0x7ffed537bc83-0x7ffed537bc80: d537bcb0 | d537bcb0
0x7ffed537bc7f-0x7ffed537bc7c: 00005629 | 00000002
0x7ffed537bc7b-0x7ffed537bc78: 9882b460 | 9882b460
0x7ffed537bc77-0x7ffed537bc74: 00007ffe | 00007ffe
0x7ffed537bc73-0x7ffed537bc70: d537be28 | d537be28

(7)
start process: 691397, start parent process: 691396
finish process: 691397, finish parent process: 691396
function: 0x5629986b42a1, caller: 0x5629986b4288, frame pointer: 0x7ffed537bcb0
stack frame: 0x7ffed537bcc0-0x7ffed537bca0, time: 0.001039089 (249512.354664126-249512.355703215)
address range                  initial    final
0x7ffed537bcbf-0x7ffed537bcbc: 00005629 | 00005629
0x7ffed537bcbb-0x7ffed537bcb8: 986b4288 | 986b4288
0x7ffed537bcb7-0x7ffed537bcb4: 00007ffe | 00007ffe
0x7ffed537bcb3-0x7ffed537bcb0: d537bce0 | d537bce0
0x7ffed537bcaf-0x7ffed537bcac: 00005629 | 00000003
0x7ffed537bcab-0x7ffed537bca8: 9882b3b0 | 9882b3b0
0x7ffed537bca7-0x7ffed537bca4: 00007ffe | 00007ffe
0x7ffed537bca3-0x7ffed537bca0: d537be28 | d537be28

(8)
start process: 691397, start parent process: 691396
finish process: 691397, finish parent process: 691396
function: 0x5629986b4249, caller: 0x5629986b433a, frame pointer: 0x7ffed537bce0
stack frame: 0x7ffed537bcf0-0x7ffed537bcd0, time: 0.001161351 (249512.354645315-249512.355806666)
address range                  initial    final
0x7ffed537bcef-0x7ffed537bcec: 00005629 | 00005629
0x7ffed537bceb-0x7ffed537bce8: 986b433a | 986b433a
0x7ffed537bce7-0x7ffed537bce4: 00007ffe | 00007ffe
0x7ffed537bce3-0x7ffed537bce0: d537bd10 | d537bd10
0x7ffed537bcdf-0x7ffed537bcdc: 00007ffe | 00000004
0x7ffed537bcdb-0x7ffed537bcd8: d537be38 | d537be38
0x7ffed537bcd7-0x7ffed537bcd4: 00000000 | 00000000
0x7ffed537bcd3-0x7ffed537bcd0: 00000000 | 00000000

(9)
start process: 691396, start parent process: 592583
finish process: 691397, finish parent process: 691396
function: 0x5629986b42f7, caller: 0x7f0bbfef3290, frame pointer: 0x7ffed537bd10
stack frame: 0x7ffed537bd20-0x7ffed537bd00, time: 0.001813362 (249512.354067438-249512.355880800)
address range                  initial    final
0x7ffed537bd1f-0x7ffed537bd1c: 00007f0b | 00007f0b
0x7ffed537bd1b-0x7ffed537bd18: bfef3290 | bfef3290
0x7ffed537bd17-0x7ffed537bd14: 00000000 | 00000000
0x7ffed537bd13-0x7ffed537bd10: 00000001 | 00000001
0x7ffed537bd0f-0x7ffed537bd0c: 00007ffe | 00007ffe
0x7ffed537bd0b-0x7ffed537bd08: d537be28 | d537be28
0x7ffed537bd07-0x7ffed537bd04: 00000000 | 00000000
0x7ffed537bd03-0x7ffed537bd00: 00000000 | 00000000

(10)
start process: 691398, start parent process: 691396
finish process: 691398, finish parent process: 691396
function: 0x5629986b42a1, caller: 0x5629986b4288, frame pointer: 0x7ffed537bcb0
stack frame: 0x7ffed537bcc0-0x7ffed537bca0, time: 0.000884282 (249512.354741777-249512.355626059)
address range                  initial    final
0x7ffed537bcbf-0x7ffed537bcbc: 00005629 | 00005629
0x7ffed537bcbb-0x7ffed537bcb8: 986b4288 | 986b4288
0x7ffed537bcb7-0x7ffed537bcb4: 00007ffe | 00007ffe
0x7ffed537bcb3-0x7ffed537bcb0: d537bce0 | d537bce0
0x7ffed537bcaf-0x7ffed537bcac: 00005629 | 00000003
0x7ffed537bcab-0x7ffed537bca8: 9882b3b0 | 9882b3b0
0x7ffed537bca7-0x7ffed537bca4: 00007ffe | 00007ffe
0x7ffed537bca3-0x7ffed537bca0: d537be28 | d537be28

(11)
start process: 691398, start parent process: 691396
finish process: 691398, finish parent process: 691396
function: 0x5629986b4249, caller: 0x5629986b433a, frame pointer: 0x7ffed537bce0
stack frame: 0x7ffed537bcf0-0x7ffed537bcd0, time: 0.001317149 (249512.354724060-249512.356041209)
address range                  initial    final
0x7ffed537bcef-0x7ffed537bcec: 00005629 | 00005629
0x7ffed537bceb-0x7ffed537bce8: 986b433a | 986b433a
0x7ffed537bce7-0x7ffed537bce4: 00007ffe | 00007ffe
0x7ffed537bce3-0x7ffed537bce0: d537bd10 | d537bd10
0x7ffed537bcdf-0x7ffed537bcdc: 00007ffe | 00000004
0x7ffed537bcdb-0x7ffed537bcd8: d537be38 | d537be38
0x7ffed537bcd7-0x7ffed537bcd4: 00000000 | 00000000
0x7ffed537bcd3-0x7ffed537bcd0: 00000000 | 00000000

(12)
start process: 691396, start parent process: 592583
finish process: 691398, finish parent process: 691396
function: 0x5629986b42f7, caller: 0x7f0bbfef3290, frame pointer: 0x7ffed537bd10
stack frame: 0x7ffed537bd20-0x7ffed537bd00, time: 0.002054429 (249512.354067438-249512.356121867)
address range                  initial    final
0x7ffed537bd1f-0x7ffed537bd1c: 00007f0b | 00007f0b
0x7ffed537bd1b-0x7ffed537bd18: bfef3290 | bfef3290
0x7ffed537bd17-0x7ffed537bd14: 00000000 | 00000000
0x7ffed537bd13-0x7ffed537bd10: 00000001 | 00000001
0x7ffed537bd0f-0x7ffed537bd0c: 00007ffe | 00007ffe
0x7ffed537bd0b-0x7ffed537bd08: d537be28 | d537be28
0x7ffed537bd07-0x7ffed537bd04: 00000000 | 00000000
0x7ffed537bd03-0x7ffed537bd00: 00000000 | 00000000

(13)
start process: 691396, start parent process: 592583
finish process: 691396, finish parent process: 592583
function: 0x5629986b42f7, caller: 0x7f0bbfef3290, frame pointer: 0x7ffed537bd10
stack frame: 0x7ffed537bd20-0x7ffed537bd00, time: 0.002472131 (249512.354067438-249512.356539569)
address range                  initial    final
0x7ffed537bd1f-0x7ffed537bd1c: 00007f0b | 00007f0b
0x7ffed537bd1b-0x7ffed537bd18: bfef3290 | bfef3290
0x7ffed537bd17-0x7ffed537bd14: 00000000 | 00000000
0x7ffed537bd13-0x7ffed537bd10: 00000001 | 00000001
0x7ffed537bd0f-0x7ffed537bd0c: 00007ffe | 00007ffe
0x7ffed537bd0b-0x7ffed537bd08: d537be28 | d537be28
0x7ffed537bd07-0x7ffed537bd04: 00000000 | 00000000
0x7ffed537bd03-0x7ffed537bd00: 00000000 | 00000000
```

### Compilation and makefile
Compilation and makefile requirements are similar to the last project. The only new requirement is to add options `-g -Wall` to all your `gcc` commands:
- Using option `-Wall` you will see all code warnings. You should try to resolve all warnings. The only warnings that should remain is regarding the unsafe use of `_builtin_frame_address(1)` (which we intentionally ignore in this project).
- Using option `-g` the compiler will include the debugging information in your executable so that you can use `gdb` and `valgrind` for debugging.

> **Warning:** Missing the following compilation instructions can result in segmentation faults. Essentially, we need to avoid instrumenting our instrumentation code! Otherwise, it will turn it to a recursive function call that will eventually crash our program. 

It is critical to compile the instrumentation code separately in this project, and then compile/link it together with the source of your target program. For example, in order to generate an instrumented executable for the `testfork.c` program, you need to do the followings:
```
gcc -c inst.c
gcc -finstrument-functions testfork.c inst.o -o testfork-inst
```

Create a makefile similar to the last project to facilitate rebuilding the project and creating executables `testpipe`, `testpipe-inst`, `testfork`, `testfork-inst`, and `stackviz` by just typing `make`.

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
Review this tentative rubric to make sure that you have not missed instructions. There are 140 total points. (Your score is calculated out of 100; 40 points is bonus).

| Rubric                                                                                                   | Poor                                    | OK                                             | Good                                                 | Great                                                                                  |
|----------------------------------------------------------------------------------------------------------|-----------------------------------------|------------------------------------------------|------------------------------------------------------|----------------------------------------------------------------------------------------|
| Code indentation                                                                                         | (0) No indentation                      | (2) Existing but inconsistent indentation      |                                                      | (3) Correct indentation throughout                                                     |
| Comments                                                                                                 | (0) None/Excessive                      | (1) "what", not "why" comments, few            | (2) Some "what" comments or missing some             | (3) Anything not obvious has reasoning                                                 |
| Function Documentation                                                                                   | (0) None                                | (1) Few and/or missing parameter/return        | (2) Most exist with most pieces in place             | (3) All functions have clear description, parameters and return values clearly defined |
| Variable/Function Naming                                                                                 | (0) Single letters everywhere           | (1) Lots of abbreviations                      | (2) Full words most of the time                      | (3) Full words, descriptive                                                            |
| `inst.c`: capture frame pointer, begin/end address                                                       | (0) None                                | (2) One of the three                           | (4) Two of the three                                 | (6) All                                                                                |
| `inst.c`: capture initial contents (in function's enter)                                                 | (0) No                                  | (2) Allocated, but not copied correctly        |                                                      | (5) Allocated and copied correctly                                                     |
| `inst.c`: capture final contents (in function's exit)                                                    | (0) No                                  | (2) Allocated, but not copied correctly        |                                                      | (5) Allocated and copied correctly                                                     |
| `inst.c`: capture start and finish PID/PPID (in function's enter/exit)                                   | (0) No                                  | (3) One of the two                             |                                                      | (6) Both                                                                               |
| `inst.c`: capture time using clock_gettime() (in function's enter/exit)                                  | (0) No                                  | (2) One of the two                             |                                                      | (4) Both                                                                               |
| `inst.c`: store stack frames in call finish order (in function's exit) in stack.bin                      | (0) No                                  | (2) Partially                                  | (4) All, but not in correct order                    | (5) All and in correct order                                                           |
| `inst.c`: store initial/final contents of stack frames in stack.bin                                      | (0) No                                  | (5) One of the two                             |                                                      | (10) Both                                                                              |
| `stackviz.c`: print index (counter) for stack frames (always start from 1)                               | (0) No                                  | (2) Shows counter but not always correctly     |                                                      | (4) Correct                                                                            |
| `stackviz.c`: print PID/PPID of start and finish process                                                 | (0) None                                | (2) Partially Correct                          |                                                      | (4) Correct                                                                            |
| `stackviz.c`: print function, caller, and frame pointer                                                  | (0) None                                | (1) One of the three                           | (2) Two of the three                                 | (3) All                                                                                |
| `stackviz.c`: print correct stack frame range                                                            | (0) None                                | (2) Partially correct range                    |                                                      | (4) Correct                                                                            |
| `stackviz.c`: print correct time span (begin-end; begin should be < end; difference not needed)          | (0) None                                | (2) Partially correct time spans               |                                                      | (4) Correct                                                                            |
| `stackviz.c`: print line-by-line contents: address range, initial, and final                             | (0) None                                | (3) One of the three                           | (6) Two of the three                                 | (10) All                                                                               |
| `stackviz.c`: print line-by-line contents according to provided format                                   | (0) None                                | (2) Major formatting issues                    | (4) Minor formatting issues                          | (5) Perfectly                                                                          |
| `stackviz.c`: option of order by start time                                                              | (0) Not implemented                     | (2) Option processed but not sorting correctly |                                                      | (5) Works correctly                                                                    |
| `stackviz.c`: option of order by finish time                                                             | (0) Not implemented                     | (2) Option processed but not sorting correctly |                                                      | (5) Works correctly                                                                    |
| `stackviz.c`: option of order by finish PID and start time                                               | (0) Not implemented                     | (2) Option processed but not sorting correctly |                                                      | (5) Works correctly                                                                    |
| `inst.c`/`stackviz.c`: store and display stack frames of correct function calls in two test cases        | (0) Very different from expected output | (5) Captures most, but not all calls           | (10) Captures all calls in test1 and most in test2   | (20) Captures all calls in both cases                                                  |
| Two-line compilation: work as expected; can produce instrumented `testpipe-inst`/`testfork-inst`         | (0) No                                  |                                                |                                                      | (10) Yes                                                                               |
| `makefile`: include options `-g` and `-Wall` in all compilations                                         | (0) No                                  | (1) Only included in a few cases               | (2) Included in most, but not all cases              | (3) Included in all cases                                                              |
| `makefile`: produce executables `testpipe`, `testpipe-inst`, `testfork`, `testfork-inst`, and `stackviz` | (0) None                                | (2) Only produces two of the five correctly    | (4) Produces four of the five correctly              | (5) Works correctly                                                                    |
