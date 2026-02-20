# Module 4: Debugging with Claude -- Solutions

## Bug Summary

| Bug | File                  | Symptom                         | Root Cause                                          | Fix                       |
| --- | --------------------- | ------------------------------- | --------------------------------------------------- | ------------------------- |
| #1  | `TaskController.java` | POST returns 200 instead of 201 | `ResponseEntity.ok()` instead of `status(CREATED)`  | Use `HttpStatus.CREATED`  |
| #2  | `TaskService.java`    | Partial update nullifies fields | Unconditional `setTitle()` etc. without null checks | Add null guards           |
| #3  | `api.ts`              | Delete button doesn't delete    | `method: "GET"` instead of `"DELETE"`               | Change to `"DELETE"`      |
| #4  | `data.sql`            | App crashes on startup          | Invalid enum value `'PROGRESS'`                     | Change to `'IN_PROGRESS'` |

## Exercise 1: Share the Actual Error (Bug #4)

### The stack trace

```bash
cd backend && ./mvnw spring-boot:run
```

The app crashes during startup. The key line in the stack trace:

```text
Caused by: org.h2.jdbc.JdbcSQLDataException: Data conversion error
converting "'PROGRESS' (TASKS: STATUS ENUM(...))"
```

### Root cause

`data.sql` line 5 inserts a seed record with status `'PROGRESS'`, but the `TaskStatus` enum defines only `TODO`, `IN_PROGRESS`, and `DONE`. Hibernate can't map the string to a valid enum constant.

### Fix

In `backend/src/main/resources/data.sql`, change `'PROGRESS'` to `'IN_PROGRESS'`:

```sql
-- Before
('Write API documentation', '...', 'PROGRESS', 'MEDIUM', ...)
-- After
('Write API documentation', '...', 'IN_PROGRESS', 'MEDIUM', ...)
```

### Why the full error matters

Sharing the stack trace with Claude immediately points to `data.sql` and the invalid enum value. A vague "the app won't start" prompt would trigger a broad investigation of configuration files, dependency versions, and port conflicts -- none of which are the problem.

## Exercise 2: Backward Tracing (Bug #1)

### Reproduce

```bash
curl -s -o /dev/null -w "%{http_code}" -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Test","description":"Test task","status":"TODO","priority":"LOW"}'
# Output: 200
```

### Trace

The response originates from `TaskController.createTask()`:

```java
@PostMapping
public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
    Task created = taskService.createTask(task);
    return ResponseEntity.ok(created);  // <-- returns 200
}
```

`ResponseEntity.ok()` produces HTTP 200. The REST convention for resource creation is HTTP 201 Created.

### Fix

```java
return ResponseEntity.status(HttpStatus.CREATED).body(created);
```

### Verify

```bash
curl -s -o /dev/null -w "%{http_code}" -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Test","description":"Test task","status":"TODO","priority":"LOW"}'
# Output: 201
```

## Exercise 3: Write a Failing Test First (Bug #2 and Bug #3)

### Bug #2: Partial Update Nullifies Fields

**Failing test:**

```java
@Test
void updateStatusShouldNotNullifyTitle() {
    // Create a task
    Task task = taskRepository.save(new Task("Original Title", "Description"));

    // Send partial update: only change status
    Task statusOnly = new Task();
    statusOnly.setStatus(TaskStatus.DONE);

    Optional<Task> updated = taskService.updateTask(task.getId(), statusOnly);

    // Title should still be "Original Title", not null
    assertThat(updated).isPresent();
    assertThat(updated.get().getTitle()).isEqualTo("Original Title");
}
```

This test fails because `updateTask()` sets `task.setTitle(taskDetails.getTitle())` unconditionally. When `taskDetails.getTitle()` is null (because the request only included status), the existing title is overwritten with null.

**Fix:** Add null guards in `TaskService.updateTask()`:

```java
public Optional<Task> updateTask(Long id, Task taskDetails) {
    return taskRepository.findById(id).map(task -> {
        if (taskDetails.getTitle() != null) {
            task.setTitle(taskDetails.getTitle());
        }
        if (taskDetails.getDescription() != null) {
            task.setDescription(taskDetails.getDescription());
        }
        if (taskDetails.getStatus() != null) {
            task.setStatus(taskDetails.getStatus());
        }
        if (taskDetails.getPriority() != null) {
            task.setPriority(taskDetails.getPriority());
        }
        return taskRepository.save(task);
    });
}
```

### Bug #3: Delete Doesn't Delete

**Symptom:** Clicking delete on a task appears to succeed (no error) but the task remains.

**Root cause:** In `frontend/src/lib/api.ts`:

```typescript
export async function deleteTask(id: number): Promise<void> {
  const res = await fetch(`${API_BASE}/api/tasks/${id}`, {
    method: "GET", // Bug: should be DELETE
  });
}
```

Using `GET` instead of `DELETE` means the frontend fetches the task rather than deleting it. The response is `200 OK` (task found), so the error check passes and the UI thinks the delete succeeded.

**Fix:** Change `method: "GET"` to `method: "DELETE"`.

### Why write a test first

Writing the failing test before asking Claude to fix the bug gives Claude a concrete target: make this test pass. Without a test, Claude might fix the immediate symptom but miss the underlying issue (e.g., fixing the delete button's click handler instead of the API call's HTTP method).
