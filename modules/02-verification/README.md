# Module 2: The Verification Loop

Practice the instruct-act-review-correct cycle with explicit verification steps.

## Setup

Make sure you're on the `main` branch with the backend running:

```bash
cd backend && ./mvnw spring-boot:run
```

## Exercise 1: Verification Step in Your Prompt

Switch to the `feature/task-search` branch:

```bash
git checkout feature/task-search
```

This branch has a partially implemented task search feature. The search endpoint exists but has a bug, and the tests are failing.

Your task: Write a prompt for Claude Code that includes an explicit verification step. For example:

> "The search endpoint in TaskController.java isn't working correctly.
> Fix it, then run `./mvnw test` and fix any failures."

Run your prompt and observe how Claude uses the test output as feedback to iterate.

## Exercise 2: Plan Before You Execute

Stay on the `feature/task-search` branch. Use plan mode (Shift+Tab) to investigate:

> "How is the search feature currently implemented? What's broken and what's missing?"

Let Claude explore the code in plan mode. Then switch to implementation mode and give it specific instructions based on what you learned.

Compare: did planning first lead to a better outcome than jumping straight to implementation?

## Exercise 3: Clear Between Tasks

Complete the search feature fix from Exercise 1 or 2. Then:

1. Run `/clear` to reset context
2. Start a new task: implement the "Due Dates" feature from `exercises/01-prompting/requirements.md`
3. Notice how `/clear` prevents the search feature context from polluting the due dates task

Observe the difference in Claude's focus before and after clearing.
