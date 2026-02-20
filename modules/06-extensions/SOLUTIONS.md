# Module 6: Extensions -- Solutions

## Exercise 1: Subagent Exploration

### Subagent analysis of the security package

Prompt:

> "Use a subagent to explore the security package and explain: how are passwords stored, how do sessions work, and what security vulnerabilities exist?"

A subagent reads `AuthService.java` and `AuthController.java` in its own context window and returns a summary. Typical findings:

**Password storage:**

- Uses SHA-256 with a 16-byte random salt per user
- Salt generated with `SecureRandom` (correct)
- SHA-256 is fast, making brute force feasible -- bcrypt or Argon2 would be stronger

**Session management:**

- Session tokens are 32-byte random values via `SecureRandom` (good entropy)
- Stored in-memory `ConcurrentHashMap` (lost on restart)
- No expiration -- sessions live forever once created

**Vulnerabilities identified:**

- SHA-256 is not a password hashing function (no work factor)
- No session timeout or expiration
- No input validation on username/password (null, empty, length)
- Same error message for "user not found" and "wrong password" (good -- prevents user enumeration)
- `String.equals()` for password comparison is vulnerable to timing attacks

### Direct vs subagent comparison

**Direct approach:** Claude reads both files into the main context (~1,000 tokens), then analyzes in place. Gives line-level detail but consumes main context.

**Subagent approach:** Only the summary (~300 tokens) enters main context. The subagent's file reads are isolated. Better for preserving context budget in long sessions.

For a 2-file auth module, the difference is small. For a larger system with 10+ security-related files, subagent delegation becomes significantly more efficient.

## Exercise 2: Custom Slash Commands

### The review-security command

`.claude/commands/review-security.md` tells Claude exactly what files to read and what to check for. Running `/project:review-security` produces a structured vulnerability report without having to type the full prompt each time.

### Creating add-endpoint

`.claude/commands/add-endpoint.md` uses `$ARGUMENTS` so you can run:

```text
/project:add-endpoint GET /api/tasks/stats -- return task count by status
```

The command references `TaskController.java` as the convention example, so Claude follows the same patterns (ResponseEntity, constructor injection, @Valid).

### Why commands are effective

Commands encode two things:

1. **Knowledge** -- which files to read, what conventions to follow
2. **Workflow** -- what steps to take, what to verify

Without a command, you'd type the full instructions every time. With a command, the knowledge persists across sessions.

## Exercise 3: Composing Extensions

### The security-audit command

`.claude/commands/security-audit.md` delegates to three subagents:

1. **Password security** -- analyzes hashing, salting, comparison
2. **Session management** -- analyzes tokens, storage, expiration
3. **Input validation** -- analyzes error handling, rate limiting

Each subagent reads the same files but focuses on different aspects.

### Expected audit findings

**Critical:**

- SHA-256 for password hashing (no work factor, fast to brute force)
- No session expiration (sessions live forever)

**High:**

- `String.equals()` for password comparison (timing attack)
- No input validation on register/login (null username crashes with NPE)
- In-memory user/session storage (lost on restart)

**Medium:**

- No rate limiting on login (brute force possible)
- No password complexity requirements
- No CSRF protection

**Low:**

- Register returns 200 instead of 201
- No account lockout after failed attempts

### Composed vs monolithic comparison

| Aspect            | Composed (3 subagents)                | Single prompt                  |
| ----------------- | ------------------------------------- | ------------------------------ |
| Main context cost | ~600 tokens (3 summaries)             | ~2,000+ tokens (full analysis) |
| Depth of analysis | Each subagent focuses deeply          | Broader but shallower          |
| Parallelism       | Subagents can run in parallel         | Sequential analysis            |
| Missed issues     | Unlikely -- focused review finds more | May skim over details          |

The composed approach scales better: for a 50-file security system, three focused subagents outperform one prompt trying to analyze everything.
