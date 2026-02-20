# Module 6: Extensions

Practice using subagents, skills, and custom commands to extend Claude Code's capabilities.

## Setup

Make sure you're on the `main` branch. This module uses the authentication module in `backend/src/main/java/com/example/tasktracker/security/`.

## Exercise 1: Subagent Exploration

The auth module has enough complexity to benefit from delegated research. Try these approaches:

1. Ask Claude to use a subagent to analyze the auth module:

   > "Use a subagent to explore the security package and explain: how are passwords stored, how do sessions work, and what security vulnerabilities exist?"

2. Compare the subagent result to asking directly:

   > "Read AuthService.java and AuthController.java and tell me about the security model."

Observe:

- How much context did each approach use?
- Did the subagent catch things the direct approach missed (or vice versa)?
- Which approach would scale better for a larger auth system?

## Exercise 2: Custom Slash Commands

Look at the example commands in `.claude/commands/`. Each file defines a slash command that Claude can execute.

1. Read `review-security.md` -- this command asks Claude to review auth code for vulnerabilities
2. Try running it: type `/project:review-security` in Claude Code
3. Create your own command: write a `.claude/commands/add-endpoint.md` file that instructs Claude to add a new REST endpoint following the patterns in TaskController

Tips for writing commands:

- Be specific about what files to read and what patterns to follow
- Include acceptance criteria so Claude knows when it's done
- Reference existing code as examples of the conventions to follow

## Exercise 3: Composing Extensions

Combine what you've learned. Create a workflow where:

1. A custom command triggers a code review of the auth module
2. The review uses subagent delegation to analyze different aspects in parallel
3. Results are consolidated into a single report

Try writing a `.claude/commands/security-audit.md` that:

- Delegates password hashing review to one subagent
- Delegates session management review to another
- Asks for a consolidated findings report

Compare the quality and context cost of the composed approach vs. a single prompt asking for everything at once.
