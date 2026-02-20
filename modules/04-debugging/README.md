# Module 4: Debugging with Claude

Practice systematic debugging using the four-phase framework: understand, reproduce, investigate, fix and verify.

## Setup

Switch to the `buggy` branch:

```bash
git checkout buggy
```

This branch has 4 intentional bugs. Read `BUGS.md` for symptom descriptions -- it tells you what's broken but not why.

**Note:** Bug #4 prevents the application from starting. You'll encounter it first when you try to run the app.

## Exercise 1: Share the Actual Error

Try to start the backend:

```bash
cd backend && ./mvnw spring-boot:run
```

It will fail. Copy the full stack trace and share it with Claude:

> "The application fails to start. Here's the error: [paste full stack trace].
> What's the root cause?"

Compare this with a vague prompt like "the app won't start" -- notice how much more effective the specific error is.

## Exercise 2: Backward Tracing

After fixing Bug #4 so the app starts, reproduce Bug #1 (wrong HTTP status on create):

```bash
curl -s -o /dev/null -w "%{http_code}" -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Test","description":"Test task","status":"TODO","priority":"LOW"}'
```

Ask Claude to trace backward from the symptom:

> "POST /api/tasks returns 200 instead of 201. Trace the response path
> from the controller method to find where the status code is set."

## Exercise 3: Write a Failing Test First

Pick Bug #2 (partial update nullifies fields) or Bug #3 (delete doesn't delete). Before asking Claude to fix it:

1. Write a failing test that captures the bug
2. Share the test output with Claude
3. Ask Claude to fix the bug using only the test as guidance

Observe how having a concrete test makes debugging faster and more focused.
