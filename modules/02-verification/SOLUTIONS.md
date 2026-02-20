# Module 2: The Verification Loop -- Solutions

## Exercise 1: Verification Step in Your Prompt

### Step 1: Run the tests to see failures

```bash
git checkout feature/task-search
cd backend && ./mvnw test
```

Two tests fail:

```text
TaskControllerSearchTest.searchShouldFindTasksBySubstring:54
  Expected: a collection with size <2>
       but: collection size was <0>

TaskControllerSearchTest.searchShouldBeCaseInsensitive:62
  Expected: a collection with size <2>
       but: collection size was <0>
```

### Step 2: Read the test output

The tests tell you exactly what's wrong:

- Searching for `"CI"` returns 0 results instead of 2
- Searching for `"ci"` (lowercase) also returns 0 results

The tests expect substring matching ("CI" should match "Set up CI pipeline" and "Fix CI permissions") and case-insensitive matching ("ci" should match "CI").

### Step 3: Find the bug

In `TaskRepository.java`:

```java
// BUG: This matches exact title, not substring
List<Task> findByTitle(String title);
```

Spring Data JPA generates `WHERE title = ?` from this method name -- an exact match. No task has a title that is exactly "CI", so 0 results.

### Step 4: The fix

Change the repository method to use Spring Data's query derivation for substring + case-insensitive matching:

```java
List<Task> findByTitleContainingIgnoreCase(String title);
```

This generates `WHERE LOWER(title) LIKE LOWER('%' || ? || '%')`.

### Step 5: Verify

```bash
./mvnw test
```

All 3 tests pass.

**The prompt that drives this loop:**

> "The search endpoint in TaskController.java isn't working correctly.
> Fix it, then run `./mvnw test` and fix any failures."

The explicit verification step (`run ./mvnw test`) is what closes the loop. Without it, Claude might fix the repository method but never confirm the tests pass.

## Exercise 2: Plan Before You Execute

### Plan mode exploration

In plan mode (Shift+Tab), Claude reads through the search implementation across three files:

1. `TaskController.java` -- has `@GetMapping("/search")` endpoint, calls `taskService.searchTasks(q)`
2. `TaskService.java` -- has `searchTasks()` that calls `taskRepository.findByTitle(query)`
3. `TaskRepository.java` -- has `findByTitle(String title)` with BUG comment

Claude identifies the issue without writing any code: the repository method does exact matching instead of substring matching. The BUG comments are an extra hint, but the test output alone would be sufficient.

### Implementation mode

After planning, a precise prompt:

> "In TaskRepository.java, change `findByTitle` to `findByTitleContainingIgnoreCase`.
> In TaskService.java, update the call to use the renamed method.
> Run `./mvnw test` to verify."

### Planning vs jumping in

Planning first produces a single, focused change. Jumping in without planning can lead to:

- Adding a `@Query` annotation when a method rename suffices
- Changing the service to do in-memory filtering (loads all tasks, wastes memory)
- Missing the BUG comment and misdiagnosing the issue

The plan-first approach is better when the failure isn't immediately obvious from the error message alone.

## Exercise 3: Clear Between Tasks

### Why `/clear` matters

After fixing the search feature, your context window contains:

- `TaskRepository.java`, `TaskService.java`, `TaskController.java` contents
- Test output from `./mvnw test`
- The search fix discussion

If you then ask Claude to implement "Due Dates" from `modules/01-prompting/requirements.md`, all that search context is still loaded. Claude might:

- Modify the search endpoint instead of creating a new one
- Confuse search-related code with due-date code
- Reference the wrong files

After `/clear`, Claude starts fresh. It reads the due dates requirement, finds the relevant files, and implements without interference from the previous task.

**Key pattern:** One task per context window. Use `/clear` between unrelated tasks.
