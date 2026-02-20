# Known Bugs

This branch has 4 intentional bugs for debugging practice. Each entry describes the **symptom** -- your job is to find the root cause and fix it.

## Bug #1: Wrong HTTP Status on Create

**Symptom:** `POST /api/tasks` with a valid body returns HTTP 200 instead of 201. REST convention says a successful resource creation should return 201 Created.

**How to reproduce:**

```bash
curl -s -o /dev/null -w "%{http_code}" -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Test","description":"Test task","status":"TODO","priority":"LOW"}'
```

Expected: `201`. Actual: `200`.

## Bug #2: Partial Update Nullifies Fields

**Symptom:** Updating only a task's status wipes out its title and description. After a PUT request that only sends `status`, the task's title and description become null.

**How to reproduce:**

```bash
# Create a task
curl -s -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Important task","description":"Do not lose this","status":"TODO","priority":"HIGH"}'

# Update only the status (notice: title and description are missing from the body)
curl -s -X PUT http://localhost:8080/api/tasks/6 \
  -H "Content-Type: application/json" \
  -d '{"status":"DONE","priority":"HIGH"}'

# Check the task -- title and description are now null
curl -s http://localhost:8080/api/tasks/6
```

## Bug #3: Delete Doesn't Delete

**Symptom:** Clicking "delete" on the frontend appears to succeed but the task remains in the list. Refreshing the page shows the task is still there.

**How to reproduce:** Call delete from the frontend or check the network tab -- the request method is wrong.

## Bug #4: Application Fails to Start

**Symptom:** The application crashes on startup with a JPA/Hibernate error about an invalid enum value in the seed data.

**How to reproduce:**

```bash
cd backend && ./mvnw spring-boot:run
```

Check the stack trace for the invalid value.
