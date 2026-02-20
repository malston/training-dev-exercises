# Module 4: Debugging with Claude

Practice systematic debugging: understand, reproduce, investigate, fix.

## Setup

Switch to the `buggy` branch:

```bash
git checkout buggy
cd backend && ./mvnw spring-boot:run
```

This branch has 4 intentional bugs. See `BUGS.md` for symptoms.

## Exercise 1: Share the Actual Error

Pick a bug from `BUGS.md`. Before asking Claude to fix it, write down:

1. What should happen
2. What actually happens
3. The exact error output (stack trace, HTTP response, log message)

Then share all three with Claude. Compare: does giving Claude the full error context produce a faster fix than just saying "it's broken"?

## Exercise 2: Backward Tracing

Bug #2 in `BUGS.md` produces a `NullPointerException`. Ask Claude:

> "There's a NullPointerException at [line]. Trace the data flow backward from the exception to find where the null value originates."

Follow Claude's analysis. Does it correctly identify the root cause?

## Exercise 3: Break It, Test It, Fix It

Pick any working feature on the `main` branch:

1. Switch to main: `git checkout main`
2. Deliberately introduce a bug in the code
3. Write a failing test that captures the bug
4. Ask Claude to fix it using only the test output as guidance

Don't tell Claude what you changed. See if it can find and fix the bug from the test failure alone.
