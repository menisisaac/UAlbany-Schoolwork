# Lab 02
- Link to create your repository: https://classroom.github.com/a/7qeNE2s7
- Due date: Sep. 9 2021
- Final contents of repository (all in the root of your repository; no subdirectories; exact filenames):
  - `quotient_remainder.c`
  - `.gitignore` (optional)

---
The purpose of this homework assignment is to practice some shell commands and coding in C.

## Remarks
For all the assignments here onwards, once you click the assignment link and create your GitHub repository, you can simply clone the repository from GitHub to your local machine. In other words, you do not have to follow the operations that you learned in Task 4 of Lab01 on a regular basis (cloning will take care of it automatically).

## Task 0: Review the readings
In case you did not get to finish the assigned readings, make sure you do it now. Remember that it is always more fun and fruitful when you actually practice what you are reading; try examples from the readings and implement new functionalities based on those.

## Task 1: Pipes
One of the really interesting features of Bash (and many other shells) is the ability to take the output from one program and send it as input into another program. There are a number of small programs in the command line environment that are nicely designed for pipes.

For example:
One of my directories looks like this:
```
mphipps@LG32303-UAB440:\~/tmp$ ls
adder  bar.a  foo  pa  student.c  tmp.c  tmp.o
```
What if I wanted to see them sorted in reverse?
```
mphipps@LG32303-UAB440:~/tmp$ ls | sort -r
tmp.o
tmp.c
student.c
pa
foo
bar.a
adder
```
The output from 'ls' is sent "through the pipe" to the 'sort' command (the -r option makes it sort in reverse).

Another program that is designed for pipes is 'cat'. Originally, cat (which stands for conCATenate) was designed to join 2 or more files together. But cat with just one file prints out the file contents.

For example, I created a textFile and now want to see its contents:
```
username@machine:~$ cat textFile
This
is
a
list
of
words.
```

And, of course, we can sort those words:
```
username@machine:~$ cat textFile | sort
This
a
is
list
of
words.
```

## Task 2: Accessing your files (Windows only)
 
One thing that we did not talk about last week is accessing your "regular" folders from Linux. 
If you change directory (`cd`) to `/mnt/c` from bash, you should see your "Windows" hard drive area. This is useful because you will want to use some graphical text editor for your programming. Some people like using Notepad++; there are a lot of them out there – find one that you like. 

You can copy files from Windows to Linux, then compile and run.

For example, on my computer, I might need something like this:

```
cp /mnt/c/Users/username/Documents/ICSI333/Lab02/SomeProgram.c
```

This copies from Windows to Linux in the current working directory.
 
Another approach which works with WSL2 goes in the other direction - we can view our Linux directories from Windows.

If you open your File Explorer, go to the navigation bar and type: `\\wsl$`, this should show a folder called "Ubuntu-20.04" (or similar, depending on version of Linux you installed). From there you can navigate to your home directory `(/home/yourUserName)`. You can use this to save from your text editor to your Linux directories. You might find it useful to map a drive: https://support.microsoft.com/en-us/windows/map-a-network-drive-in-windows-10-29ce55d1-34e3-a7e2-4801-131475f9557d
 
For the next section, we will write our first two C programs.
 
## Task 3: Our First C program- Hello World
 
Your first program, "Hello World" is below. It prints its name as a message and exits. This is a classic "first program"

- Enter the following code into your text editor 

```
#include <stdio.h>
int main()
{
  // printf() displays the string inside quotation
  printf("Hello, World!\n");
  return 0;
}
```

- Save it as “helloworld.c”. 

- Access it from your Linux bash window.

- Now you can compile it using:
`gcc helloworld.c -o helloworld`

- You can then run it using:
`./helloworld`

> NOTE: Make sure that your slash leans in the right direction (/).

Once that you know that works, you can move on to the next program.
 
## Task 4: Our Second C program
Your actual assignment for this week is to make a program that asks for two numbers and prints the quotient (after dividing them) and the remainder. Submit the “quotient_remainder.c” file as your lab assignment. 
 
To do this program, you will use `printf` and the same division (`/`) and modulo (`%`) operators that you used in Java. There is one other function that you will need. In Java, you might have used a Scanner to read from the keyboard. In C, we will use a function called `scanf()`. It works like this:
``` 
scanf("%d",&number1);
```
 
The scanf() function takes a string (called, "Specifier") which tells the system what data type to expect ('%d' represents decimal, in this case) followed by the variable to put the result into. The variable needs an ampersand (`&`) in front of it. This will be explained in detail during class later.
 
The printf() function actually can use the same specification. So far you have seen:
```
printf("Hello, World!\n");
``` 
But you can use printf to print numbers (constant or variable):
```
printf("Ten looks like this: %d \n",10);`
``` 

> NOTE: The `\n` on the end is important. It is a special format specifier which adds "end of line" or a "new line character". 

In Java, you used print vs println in C. 

In your program, you have to use the same command, just with or without the \n according to the need.

## Task 5: Push your final repository
This is just a reminder that once you are done with this lab (and any future lab), you need to push your final version of the repository to GitHub. This version must include all files we asked for. In this lab, the required file is `quotient_remainder.c`. Including file `.gitignore` (if you are using it for this repository) is optional.
