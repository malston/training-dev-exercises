# Module 1: Prompting Foundations

Practice writing specific, actionable prompts that produce correct code.

## Setup

Start the backend and frontend:

```bash
cd backend && ./mvnw spring-boot:run
# In another terminal:
cd frontend && npm run dev
```

Verify the task list loads at `http://localhost:3000`.

## Exercise 1: Rewrite a Vague Prompt

Below are three vague prompts. Rewrite each one to be specific enough for Claude Code to implement correctly on the first attempt.

**Vague prompt A:** "Add filtering to the task list"

**Vague prompt B:** "Make the API faster"

**Vague prompt C:** "Improve the error handling"

For each rewrite, include:

- The exact file(s) to modify
- The specific behavior to add
- Why the change is needed
- How to verify it works

Compare your rewritten prompts against the examples in `examples.md`.

## Exercise 2: Implement a Feature with a Specific Prompt

Pick one requirement from `requirements.md` and write a prompt for Claude Code that includes:

1. The exact file to modify
2. The behavior to add
3. The reason for the change
4. How to verify it works

Run the prompt in Claude Code and evaluate whether it produced the correct result on the first attempt. If not, identify what was missing from your prompt.

## Exercise 3: Imperative vs. Question

Choose a requirement from `requirements.md`. Write two versions of a prompt:

1. **Imperative:** "Add [feature] to [file]. It should [behavior]."
2. **Question:** "How should I approach adding [feature] to the task tracker?"

Run both in Claude Code (use `/clear` between them). Compare:

- Did the imperative version produce working code?
- Did the question version give useful design guidance?
- When would you use each approach?
