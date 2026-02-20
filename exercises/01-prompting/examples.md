# Prompt Rewrite Examples

Compare your rewrites from Exercise 1 against these.

## Vague Prompt A: "Add filtering to the task list"

**Rewrite:**

> Add status filtering to the task list page. In `frontend/src/components/TaskList.tsx`, add a row of three buttons above the task list: "All", "To Do", "In Progress", "Done". Clicking a button should call `GET /api/tasks?status={STATUS}` and display only matching tasks. The "All" button calls `GET /api/tasks` with no filter. Highlight the active filter button with a different background color. The "All" filter should be active by default.

Why this is better: it names the file, the UI element, the API call, the behavior, and the default state.

## Vague Prompt B: "Make the API faster"

**Rewrite:**

> The `GET /api/tasks` endpoint loads all tasks every time, even when the caller only needs a count. Add a `GET /api/tasks/count` endpoint in `backend/src/main/java/com/example/tasktracker/controller/TaskController.java` that returns `{ "count": N }` using `taskRepository.count()`. This avoids serializing the full task list when the frontend dashboard only needs the total.

Why this is better: it identifies a specific performance problem, names the file, describes the endpoint, and explains why.

## Vague Prompt C: "Improve the error handling"

**Rewrite:**

> In `backend/src/main/java/com/example/tasktracker/controller/TaskController.java`, the `createTask` method returns a generic 500 when validation fails. Add a `@ExceptionHandler` for `MethodArgumentNotValidException` that returns a 400 response with a JSON body listing each field error: `{ "errors": [{ "field": "title", "message": "Title is required" }] }`. This gives the frontend actionable error messages instead of a stack trace.

Why this is better: it names the specific problem, the file, the exception type, the response format, and the reason.
