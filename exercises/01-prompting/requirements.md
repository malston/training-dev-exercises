# Feature Requirements

These are features the task tracker needs. Use them as inputs for prompting exercises.

## 1. Task Search

Users want to find tasks by typing a search term. The search should match against the task title (case-insensitive substring match).

- Backend: Add a `GET /api/tasks/search?q={term}` endpoint that returns matching tasks
- Frontend: Add a search input above the task list that filters results as the user types
- The search input should debounce by 300ms to avoid excessive API calls

## 2. Due Dates

Tasks need a due date field. Tasks past their due date should be visually highlighted.

- Backend: Add a `dueDate` field (optional, ISO date format) to the Task entity
- Backend: Add a `GET /api/tasks?overdue=true` filter that returns tasks past their due date
- Frontend: Display the due date on each task card
- Frontend: Tasks past their due date should have a red border

## 3. Task Comments

Users want to add comments to tasks to track discussion and progress.

- Backend: Create a `Comment` entity with `id`, `taskId`, `author`, `content`, `createdAt`
- Backend: Add `GET /api/tasks/{id}/comments` and `POST /api/tasks/{id}/comments` endpoints
- Frontend: Show a comments section on each task card with an "Add comment" form

## 4. Bulk Status Update

Users want to select multiple tasks and change their status at once.

- Backend: Add a `PATCH /api/tasks/bulk-status` endpoint that accepts `{ ids: [...], status: "DONE" }`
- Frontend: Add checkboxes to each task card and a "Change status" dropdown that applies to all selected tasks
