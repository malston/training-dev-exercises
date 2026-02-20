# Module 1: Prompting Foundations -- Solutions

## Exercise 1: Rewrite a Vague Prompt

See `examples.md` for the full rewrites. The key principles each rewrite demonstrates:

**Prompt A ("Add filtering to the task list"):**
The rewrite names the exact file (`TaskList.tsx`), the UI element (filter buttons), the API call (`GET /api/tasks?status={STATUS}`), and the default state ("All" active by default). A developer reading this knows exactly what to build without asking follow-up questions.

**Prompt B ("Make the API faster"):**
The rewrite identifies a specific performance problem (serializing the full task list when only a count is needed), names the file and method, and explains why the change helps. "Faster" is meaningless without a specific bottleneck.

**Prompt C ("Improve the error handling"):**
The rewrite names the exception type (`MethodArgumentNotValidException`), the response format (JSON with field errors), and why it matters (actionable messages instead of stack traces). "Better error handling" could mean a hundred different things.

**Pattern:** Every rewrite answers four questions: what file, what behavior, what format, and why.

## Exercise 2: Implement a Feature with a Specific Prompt

The task search feature is implemented on this branch as the worked example.

**The specific prompt that produced this implementation:**

> Add a search endpoint to the task tracker. In `TaskRepository.java`, add a
> `findByTitleContainingIgnoreCase(String title)` method. In `TaskService.java`,
> add a `searchTasks(String query)` method that calls the repository method.
> In `TaskController.java`, add a `GET /api/tasks/search?q={term}` endpoint
> that returns matching tasks. The `/search` mapping must come before `/{id}`
> so Spring doesn't interpret "search" as an ID.

**What changed:**

- `TaskRepository.java` -- added `findByTitleContainingIgnoreCase` (Spring Data derives the query from the method name)
- `TaskService.java` -- added `searchTasks` that delegates to the repository
- `TaskController.java` -- added `@GetMapping("/search")` mapped before `/{id}`

**Verification:**

```bash
curl "http://localhost:8080/api/tasks/search?q=test"
```

**Why the prompt worked on the first attempt:** It named every file, every method signature, the endpoint path, and called out the route ordering constraint (a common Spring MVC pitfall).

## Exercise 3: Imperative vs. Question

**Imperative version:**

> Add a `GET /api/tasks/search?q={term}` endpoint in TaskController.java that
> searches tasks by title using case-insensitive substring matching. Add the
> repository method and service method. Run `./mvnw test` after implementing.

Result: Claude writes the code immediately. Good when you know what you want.

**Question version:**

> How should I approach adding search functionality to the task tracker? What
> are the options for implementing search in Spring Boot with JPA?

Result: Claude discusses options -- Spring Data derived queries, `@Query` with JPQL, Specification API, full-text search with Hibernate Search. Good when you're exploring design choices.

**When to use each:**

- **Imperative** when the requirement is clear and you want code now
- **Question** when you're unsure about the approach and want to explore options before committing
- A common workflow is question first (to choose an approach), then imperative (to implement it)
