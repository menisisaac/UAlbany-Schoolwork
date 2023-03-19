# Homework 3

For the full homework description and grading
information, see the document in the main course 
repo:
[Homework 3](https://github.com/lspitzley/bfor206_spring2022/blob/main/homework/Homework%203.md).

# How to use tests 

To receive credit for a function that passes tests, it should
be in the `irc_parse.py` file, and use the names that are 
pre-defined. You can add additional tests with other names, but
these will not be graded. 

## On your machine

Open a terminal and make sure that you
are in the top-level directory for this assignment.
Then, run `pytest`.

```bash
pytest
```

This will find any files that are named with `test_*.py` and run
the test functions in those files.

To run only the tests for the assignment, run:
```bash
pytest tests/
```

You might also want to test additional functions that you make.
You can create an additional test script to test these functions.
Any new tests will not be graded. 


## Using Github Actions

Each time you push a commit, Github will automatically 
run the autograding tests. You can see the results
of the tests in `Actions` tab in your course 
repository. In there, you can see exactly 
which tests failed and why. 


