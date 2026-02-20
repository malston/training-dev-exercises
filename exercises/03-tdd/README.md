# Module 3: Test-Driven Development

Practice writing tests first, then implementing code to make them pass.

## Exercise 1: Red-Green-Refactor

Test stubs are provided in `backend/src/test/java/com/example/tasktracker/service/TaskStatisticsServiceTest.java`. These tests describe a `TaskStatisticsService` that doesn't exist yet.

1. Run the tests: `cd backend && ./mvnw test -Dtest=TaskStatisticsServiceTest`
2. Confirm they fail (Red)
3. Ask Claude to implement `TaskStatisticsService` with the minimum code to make the tests pass (Green)
4. Review the implementation and refactor if needed while keeping tests green

The test file describes exactly what the service should do. Read it before implementing.

## Exercise 2: Context Isolation

Write tests for the "Task Comments" feature from `exercises/01-prompting/requirements.md` in a fresh Claude Code session:

1. Open Claude Code and write tests for `CommentController` and `CommentService`
2. Run `/clear` or open a new session
3. In the new session, ask Claude to implement the code to make the tests pass

The tests survive across sessions because they're on disk. Claude reads them and recovers the full specification.

## Exercise 3: Replace Mocks with Real Dependencies

Open `backend/src/test/java/com/example/tasktracker/service/TaskServiceMockTest.java`. This test uses Mockito to mock `TaskRepository` -- it only tests mock behavior, not real logic.

Rewrite it to use the real `TaskRepository` with Spring's `@DataJpaTest` or `@SpringBootTest`. The test should validate actual database behavior instead of verifying mock interactions.

Compare: does the real-dependency test catch issues the mock test misses?
