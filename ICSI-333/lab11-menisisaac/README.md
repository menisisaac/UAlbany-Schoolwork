# Lab 11 - Threads and Semaphores
- Link to create your repository: https://classroom.github.com/a/FGfKisXS
- Due date: **Nov. 29, 2022 (extended)**
- Final contents of repository (all in the root of your repository; no subdirectories; exact filenames):
  - `random_thread.c`
  - `random_semaphore.c`
  - `random_semaphore_lock.c`
  - `times.txt`
  - `.gitignore` (optional)
---
## Reading Materials and Resources
- Lecture slides and readings
- inclass-threads repository

## Task 1 - Threading 
1. Write a program that creates 1,000,000 random integers 0-9. 
2. It should then create two threads, which iterate over all of the integers (one from 0-499999 and one from 500000 - 999999), summing them using a single global variable. 
3. When both threads complete, the main thread should print the sum, then sum the array itself and print the correct value.
4. Commit your `random_thread.c` file.

### Example run
`./random_thread.c` when you run this program it should print the sum of all the random intergers.

## Task 2 - Semaphore
1. Copy `random_thread.c` and make the following modification:
2. In the threads, wrap the access to the single global variable with a semaphore.
3. Notice that when run, you get a higher value than you did with `random_thread.c`. Why?
4. Run this program with the command line program `time`.
5. Commit your `random_semaphore.c` file.

### Example run
`time ./random_semaphore`

## Task 3 - Semaphore with lock
1. Copy `random_thread.c` and make the following modification:
2. Change the threads to use a local variable for summing. At the end, add that sum to the global sum (controlling access with a semaphore like task 2). 
3. Run this program with the command line program `time`
4. How does the time of `random_semaphore_lock.c` compare to the time of `random_semaphore.c`?
5. Commit your `random_semaphore_lock.c` file.
### Example run
`time ./random_semaphore_lock`

## Task 4 - Create a text file: times.txt
1. Answer the why question from task 2.
2. Record the times from tasks 2 and tasks 3.
3. Speculate on why there is a time difference between tasks 2 and 3.

## Rubric:
- (`random_thread.c`) creating 2 threads (2pt.)
- (`random_thread.c`) generating and summing random numbers (1pt.)
- (`random_semaphore.c`) creating semaphore and locking with it (2pt.)
- (`random_semaphore_lock.c`) creating semaphore and locking with it (2pt.)
- (`random_semaphore_lock.c`) using a local summing variable (1pt.)
- a short document (text file: `times.txt`) that show the times for each program(2pt.)

