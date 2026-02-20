# Module 5: Context Management

Practice understanding what Claude sees, what it forgets, and how to optimize for long sessions.

## Setup

Make sure you're on the `main` branch with the full codebase available.

## Exercise 1: Measure Context Cost

Start a Claude Code session in this repo. Observe the token counter as you work:

1. Ask Claude to read `backend/src/main/java/com/example/tasktracker/controller/TaskController.java` -- note the token count before and after
2. Ask Claude to read the entire `backend/src/main/java/com/example/tasktracker/` directory -- note the jump
3. Ask Claude to read `frontend/src/lib/api.ts` -- compare the cost of a small file vs. the full directory

Record your observations: how many tokens does each file read cost? At what point would you hit context limits?

## Exercise 2: Delegate Research to a Subagent

Ask Claude to use a subagent for a broad codebase question:

> "Use a subagent to explore the backend codebase and summarize: what design patterns are used, how is the code organized, and what conventions does it follow?"

Then ask the same question directly (without a subagent) and compare:

- How many tokens did the subagent approach cost in your main context?
- How many tokens did the direct approach cost?
- Was the quality of the answer different?

## Exercise 3: Persist Decisions

Make a design decision during your session (e.g., "use DTOs instead of exposing entities directly" or "add request validation at the controller level").

1. Write the decision to the `CLAUDE.md` file under the "Design Decisions" section
2. Run `/compact` to simulate context loss
3. Ask Claude about the decision you just made

The decision survives compaction because it's in CLAUDE.md. Compare: what happens if you only discuss the decision in conversation without writing it down?
