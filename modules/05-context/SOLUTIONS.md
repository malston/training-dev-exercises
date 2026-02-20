# Module 5: Context Management -- Solutions

## Exercise 1: Measure Context Cost

### Typical measurements

Token costs vary by model and exact file contents, but here are representative numbers from this codebase:

| File                                           | Lines | Approx. Tokens |
| ---------------------------------------------- | ----- | -------------- |
| `TaskController.java`                          | 70    | ~500           |
| `TaskService.java`                             | 57    | ~400           |
| `TaskRepository.java`                          | 13    | ~100           |
| `Task.java`                                    | 67    | ~500           |
| `api.ts`                                       | 80    | ~600           |
| Full `tasktracker/` directory (all Java files) | ~300  | ~2,500         |

### Key observations

**Rule of thumb:** 1 line of code costs roughly 5-10 tokens. A 500-line file costs 3,000-5,000 tokens.

**The context window is 200K tokens.** With the system prompt consuming ~15,000-20,000 tokens, you have ~180K tokens for conversation. Reading the full backend directory uses about 1.5% of your budget per read.

**The trap:** Asking Claude to "read the whole project" can consume 10-20% of your context in a single operation. This leaves less room for conversation, tool outputs, and Claude's thinking.

**The strategy:** Read only the files you need. If you're fixing a bug in `TaskController.java`, read that file -- not the whole `tasktracker/` directory.

## Exercise 2: Delegate Research to a Subagent

### The subagent approach

Prompt:

> "Use a subagent to explore the backend codebase and summarize: what design patterns are used, how is the code organized, and what conventions does it follow?"

Claude launches a subagent (Task tool with `subagent_type=Explore`). The subagent reads through the codebase in its own context window, then returns a summary. Only the summary enters your main context.

**Cost in main context:** ~200-400 tokens (the summary)

### The direct approach

Prompt:

> "Explore the backend codebase and summarize: what design patterns are used, how is the code organized, and what conventions does it follow?"

Claude reads every file directly into your main context. Each file read adds to your token usage.

**Cost in main context:** ~3,000-5,000 tokens (all the file contents plus Claude's analysis)

### Comparison

| Approach | Main context cost   | Quality                                            |
| -------- | ------------------- | -------------------------------------------------- |
| Subagent | ~200-400 tokens     | Summary-level, may miss details                    |
| Direct   | ~3,000-5,000 tokens | Full detail, file contents available for follow-up |

**When to use subagents:** Broad exploration questions, background research, anything where you need a summary rather than raw code.

**When to read directly:** Targeted work on specific files, debugging where you need line-by-line context, implementation where you'll reference the code.

### What the codebase summary would report

The task tracker backend follows a layered Spring Boot architecture:

- **Controller layer** (`controller/`): REST endpoints with `@RestController`, validation via `@Valid`
- **Service layer** (`service/`): Business logic, delegates to repositories
- **Repository layer** (`repository/`): Spring Data JPA interfaces with derived query methods
- **Model layer** (`model/`): JPA entities with validation annotations, enums for status and priority
- **Convention:** Constructor injection, `ResponseEntity` return types, `Optional` for nullable lookups

## Exercise 3: Persist Decisions

### Writing the decision

Add to the project's `CLAUDE.md`:

```markdown
## Design Decisions

- Use DTOs for API responses instead of exposing JPA entities directly.
  Reason: prevents leaking database implementation details (like @Id strategy)
  and allows the API contract to evolve independently of the data model.
```

### Before compaction

Ask Claude: "What design decisions have we made about the API response format?"

Claude recalls the DTO decision from conversation history.

### After `/compact`

Ask the same question. Claude reads `CLAUDE.md` (which survives compaction because it's in the system prompt) and still knows about the DTO decision.

### Without CLAUDE.md

If the decision was only discussed in conversation (never written to CLAUDE.md), it disappears after compaction. Claude would answer "I don't have any context about design decisions for this project."

### The pattern

1. Make a decision in conversation
2. Write it to `CLAUDE.md` immediately
3. The decision persists across compaction, session restarts, and even new sessions

This is especially important for:

- **Architecture choices** (DTO pattern, error handling strategy, naming conventions)
- **Constraints** (don't use library X, always validate at controller level)
- **Preferences** (use constructor injection, prefer `Optional` over null)
