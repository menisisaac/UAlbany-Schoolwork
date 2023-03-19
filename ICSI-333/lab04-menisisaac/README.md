# Lab 04: Pointers & Strings

- Link to create your repository: https://classroom.github.com/a/ll8vdGVx
- Due date: Sep. 23, 2022
- Final contents of repository (all in the root of your repository; no subdirectories; exact filenames):
  - `pointer.c`
  - `word_length.c`
  - `.gitignore` (optional)

---

## Reading Materials and Resources

- Lecture slides and readings
- See `pointer-param.c` in `inclass-c` repo for an example of how to work with pointers.

## Task 1: Learn about Pointers
As we discussed in class, a pointer is a lot like a Java reference - it is a variable that holds the location of a value.

To declare a pointer, we use an asterisk (`*`) in the declaration of the variable:

```
int *a;
```

- `a` is a pointer to an integer.

When using a pointer, if we want to use the referenced "thing" (the integer in the case above), we dereference the pointer using the asterisk (`*`):

```
*a = 5;
```

- Puts `5` into the place that `a` points to.

Simply performing the above two statements is dangerous, because we have not told the computer where `a` is pointing to, yet we store some number there! We should either dynamically allocate a variable to which `a` points to, or assign it the address of an integer variable. To assign the address, we use the ampersand (`&`) operator to get _the address of_ another variable:

```
int b;
int *a;
a = &b;
```

- `a` now points to the same memory that `b` does.

Another option is to dynamically allocate memory for an integer a variable and let `a` points to that. We will try that in a future lab.

## Task 2: Practice with Pointers
1. Create a program (`pointer.c`) that allocates two `int` variables (`v1`, `v2`).
2. Create two pointers (`p1`, `p2`) where `p1` points to `v1` and `p2` points to `v2`. (1pt)
3. Assign 10 to `v1`.
4. Dereference the second pointer to assign 4 to `v2`. (2pt)
5. Print all four values: `v1` and `v2` and the values to which `p1` and `p2` refer. (2pt)
6. Commit your `.c` file. (Just a reminder)

### Example Run

```
./pointer
Value of variable v1: 10
Value of variable v2: 4
Value refered to by pointer p1: 10
Value refered to by pointer p2: 4
```

## Task 3: Learn about Strings
A string is not an object in C like it is in Java - there are no objects in C. Instead, a string is an array of characters. This can be both intuitive (what's a word? A bunch of letters) and very confusing since there is nothing that connects the functions that help with strings to the string 'type'. We use pointers to the beginning of the array for manipulating strings.

In C, a string is not defined by its length. Instead, we put a `'\0'` character (null) at the end of the string. Every time we do something with the string, we look for the `'\0'` character as the clue to stop. Such a character/value is called a sentinel or flag value. For this reason, we say that strings are null-terminated arrays of characters. As an example, the  `strlen()` function in the standard library uses that clue to calculate the length of a string.

Another thing that some people find confusing about strings in C is that the char type can be considered as a number as well as a character. Literally, the only difference is in interpretation:

```
char a = 65;
printf ("As a character: %c, as a number: %d \n", a, a);
```

- will print the character whose ASCII value is equal to 65

```
As a character: A, as a number: 65
```
## Task 4: Word Length
Create a program (`word_length.c`) as follows:
1. Using `scanf()`, read a string from the user. Assume a maximum length of 100 characters. (Do not provide any spaces as input.) (1pt)
2. Calculate the length of the string (without using standard functions). (3pt)
3. Print your calculated length of the string, followed by the length produced by the standard functions. (1pt)
4. Commit your `.c` file. (Just a reminder)

### Example Run
```
./word_length
Enter the string: This_is_ICSI-333_lab
Length of string without using standard functions: 20
Length of string using standard functions: 20
```
> Notes:
> - Scanf has a description of `%s` that looks like this: Matches a sequence of non-white-space characters; the next pointer must be a pointer to the initial element of a character array that is long enough to hold the input sequence and the terminating null byte (`'\0'`), which is added automatically.  The input string stops at white space or at the maximum field width, whichever occurs first.
> - Because of this, your input must be ONLY a single word long and end in a space.
> - scanf will *happily* exceed the length of your array and write into memory that is not allocated. Be careful. Note that you would never use scanf this way in real life, for that very reason. 
