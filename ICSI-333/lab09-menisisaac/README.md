# Lab 09: Pipes and Signals
- Link to create your repository: https://classroom.github.com/a/qGkDjhUX
- Due date: Nov. 11, 2022
- Final contents of repository (all in the root of your repository; no subdirectories; exact filenames):
  - `pipes_signals.c`
  - `.gitignore` (optional)

---

## Reading Materials and Resources
- Lecture slides and readings
- `inclass-pipe` repository (`pipe2.c` and `sigint.c`, in particular)

## Task: Process Communication using pipes
Write a program (`pipes_signals.c`) where:

1. Your `main()` function must
    1. create two pipes and make them global.
    2. create two child processes. Each child process will call one of the functions defined in (2) and (3) below.
    3. `wait()` for a child process to end. Then, send the `SIGUSR1` signal to the (2) process before ending. 
2. One function should have an integer variable that starts at 0. It should print `ping: {value}` then increment the value. It should write that value to a pipe and read the value back from the other pipe until the value is greater than or equal to 100. It should call `exit()` when complete.
3. The other function should set up a signal handler for `SIGUSR1` to call a function (defined in (4) below) when it receives that signal. It should then loop forever: read from a pipe, print `pong-{value}`, increment the value and write the value to the other pipe. These pipes must be opposite from the function in (2): the pipe you write to in (2) must be the pipe that you read from in (3) and vice versa.
4. Create a function for the signal handler that should print `pong quitting` and then `exit()`.

## Example Run:
```
./pipes_signals
ping: 0
pong: 1
ping: 2
pong: 3
ping: 4
pong: 5
ping: 6
pong: 7
ping: 8
pong: 9
ping: 10
pong: 11
ping: 12
pong: 13
ping: 14
pong: 15
ping: 16
pong: 17
ping: 18
pong: 19
ping: 20
pong: 21
ping: 22
pong: 23
ping: 24
pong: 25
ping: 26
pong: 27
ping: 28
pong: 29
ping: 30
pong: 31
ping: 32
pong: 33
ping: 34
pong: 35
ping: 36
pong: 37
ping: 38
pong: 39
ping: 40
pong: 41
ping: 42
pong: 43
ping: 44
pong: 45
ping: 46
pong: 47
ping: 48
pong: 49
ping: 50
pong: 51
ping: 52
pong: 53
ping: 54
pong: 55
ping: 56
pong: 57
ping: 58
pong: 59
ping: 60
pong: 61
ping: 62
pong: 63
ping: 64
pong: 65
ping: 66
pong: 67
ping: 68
pong: 69
ping: 70
pong: 71
ping: 72
pong: 73
ping: 74
pong: 75
ping: 76
pong: 77
ping: 78
pong: 79
ping: 80
pong: 81
ping: 82
pong: 83
ping: 84
pong: 85
ping: 86
pong: 87
ping: 88
pong: 89
ping: 90
pong: 91
ping: 92
pong: 93
ping: 94
pong: 95
ping: 96
pong: 97
ping: 98
pong: 99
Pong quitting
```
## Rubric:
- Make 2 processes (2pt.)
- Create 2 pipes (2pt.)
- Process 1 increments a counter and  prints, exiting appropriately (1pt.)
- Process 1 sends counter to process 2 (1pt.)
- Process 2 reads counter value and prints it (1pt.)
- Process 2 sends counter back to process 1 (1pt.)
- Create signal handler to exit process 2 (1pt.)
- wait() and send signal to process 2 in main (1pt.)
