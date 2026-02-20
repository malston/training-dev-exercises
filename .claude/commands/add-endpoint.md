Add a new REST endpoint to the task tracker following existing conventions.

Read `backend/src/main/java/com/example/tasktracker/controller/TaskController.java` as the reference for conventions:

- Use `@RestController` with `@RequestMapping`
- Return `ResponseEntity<T>` from every method
- Use constructor injection for services
- Use `@Valid` on request body parameters
- Return 201 for creation, 200 for reads, 204 for deletes, 404 when not found

The endpoint to add: $ARGUMENTS

After implementation:

1. Run `./mvnw test` to verify no regressions
2. Show a sample curl command to test the endpoint
