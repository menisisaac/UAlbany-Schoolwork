# Lab 08: Processes
- Link to create your repository: https://classroom.github.com/a/4EH46mKj
- Due date: **Nov. 7, 2022** (extended)
- Final contents of repository (all in the root of your repository; no subdirectories; exact filenames):
  - `myexec.c`
  - `.gitignore` (optional)

---

## Reading Materials and Resources
- Lecture slides and readings
- `inclass-process` repository

## Task: Forking and Executing
Write a program (`myexec.c`) that
1. As its input arguments, accepts a program name followed by any number of command line arguments for that program.
2. Creates a child process that executes the given program, passing all the provided arguments to it.
3. Once the execution of the child process finishes, the **parent process** receives the child process exit code (`X`) and prints `Exit code was X` to the **standard error device**.

## Example run
If you run `./myexec echo this is just a test`, it should produce
```
this is just a test
```
on the standard output (note: this printout is produced by the `echo` program in this example), and
```
Exit code was 0
```
on the standard error device.

## Rubric
- Creating child (2pt.)
- Executing the given program (2pt.)
- Passing all the arguments correctly (2pt.)
- Waiting for child's termination (2pt.)
- Retrieving child's exit code and printing it to standard error (2pt.)
