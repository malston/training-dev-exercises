# Module 3: Test-Driven Development -- Solutions

## Exercise 1: Red-Green-Refactor

### Red: Confirm tests fail

```bash
cd backend && ./mvnw test -Dtest=TaskStatisticsServiceTest
```

All 4 tests fail with `UnsupportedOperationException` -- the stub methods throw instead of computing.

### Green: Implement minimum code

The tests specify three methods:

**`countByStatus()`** -- Group tasks by status and count each.

```java
public Map<TaskStatus, Long> countByStatus() {
    List<Task> tasks = taskRepository.findAll();
    return tasks.stream()
            .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));
}
```

**`completionRate()`** -- Percentage of tasks with status DONE. Returns 0.0 when no tasks exist.

```java
public double completionRate() {
    List<Task> tasks = taskRepository.findAll();
    if (tasks.isEmpty()) {
        return 0.0;
    }
    long done = tasks.stream()
            .filter(t -> t.getStatus() == TaskStatus.DONE)
            .count();
    return (done * 100.0) / tasks.size();
}
```

**`countByPriority()`** -- Group tasks by priority and count each.

```java
public Map<TaskPriority, Long> countByPriority() {
    List<Task> tasks = taskRepository.findAll();
    return tasks.stream()
            .collect(Collectors.groupingBy(Task::getPriority, Collectors.counting()));
}
```

### Verify: All tests pass

```bash
./mvnw test -Dtest=TaskStatisticsServiceTest
# Tests run: 4, Failures: 0, Errors: 0
```

### Refactor observations

The three methods share a common pattern: load all tasks, then group by a property. For a production system, you'd push the grouping to the database with a custom `@Query`. For this exercise, the stream-based approach is sufficient and directly maps to the test expectations.

## Exercise 2: Context Isolation (Task Comments)

### Session 1: Write tests first

The Comment feature requirements (from `modules/01-prompting/requirements.md`):

- `Comment` entity: `id`, `taskId`, `author`, `content`, `createdAt`
- `GET /api/tasks/{id}/comments` -- list comments for a task
- `POST /api/tasks/{id}/comments` -- add a comment to a task

The tests written in session 1 (`CommentControllerTest.java`) cover:

1. **Empty list for new task** -- GET returns `[]` when no comments exist
2. **Create and return** -- POST creates a comment with correct fields and returns 201
3. **List multiple** -- GET returns all comments in creation order
4. **Nonexistent task** -- POST to a nonexistent task returns 400

### Session 2: Implement to pass tests

After `/clear`, Claude reads the test file and recovers the specification. The implementation requires:

**`Comment.java` entity:**

```java
@Entity
@Table(name = "comments")
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long taskId;
    private String author;
    private String content;
    private LocalDateTime createdAt;
    // ...
}
```

**`CommentRepository.java`:**

```java
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTaskIdOrderByCreatedAtAsc(Long taskId);
}
```

**`CommentService.java`:**

```java
public List<Comment> getComments(Long taskId) {
    return commentRepository.findByTaskIdOrderByCreatedAtAsc(taskId);
}

public Comment addComment(Long taskId, Comment comment) {
    if (!taskRepository.existsById(taskId)) {
        throw new IllegalArgumentException("Task not found: " + taskId);
    }
    comment.setTaskId(taskId);
    return commentRepository.save(comment);
}
```

**`CommentController.java`:**

```java
@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
public class CommentController {
    @GetMapping
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long taskId) { ... }

    @PostMapping
    public ResponseEntity<Comment> addComment(@PathVariable Long taskId,
            @Valid @RequestBody Comment comment) { ... }
}
```

### Why context isolation works

The tests are the specification. When Claude reads `CommentControllerTest.java` in a fresh session, it sees:

- The endpoint paths (`/api/tasks/{taskId}/comments`)
- The HTTP methods (GET, POST)
- The request/response format (JSON with author, content, taskId fields)
- The edge case (nonexistent task returns 400)

No prior conversation context is needed -- the test file carries the full specification.

## Exercise 3: Replace Mocks with Real Dependencies

### The mock test problem

`TaskServiceMockTest.java` uses Mockito to mock `TaskRepository`. Every test follows the same pattern:

```java
when(taskRepository.findAll()).thenReturn(mockTasks);
List<Task> result = taskService.getAllTasks();
verify(taskRepository).findAll();
```

This tests that `getAllTasks()` calls `findAll()` -- but that's an implementation detail. If the service changed to use a different query method (like `findAllByOrderByCreatedAtDesc()`), the mock test would fail even though the behavior is correct.

### The real-dependency replacement

`TaskServiceTest.java` uses `@SpringBootTest` with the real H2 database:

```java
@SpringBootTest
class TaskServiceTest {
    @Autowired private TaskService taskService;
    @Autowired private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void createTaskShouldPersistAndReturnTask() {
        Task input = new Task("Test Task", "Test Description");
        Task result = taskService.createTask(input);

        assertThat(result.getId()).isNotNull();
        assertThat(taskRepository.findById(result.getId())).isPresent();
    }
}
```

### What the real test catches that the mock misses

- **Actual persistence**: The mock test never checks that `save()` actually persists to a database. The real test confirms the task appears in `findById()` after creation.
- **Delete cascade**: The mock test verifies `delete()` was called but doesn't check the record is gone. The real test confirms `findById()` returns empty after deletion.
- **Update correctness**: The mock test doesn't verify the updated fields are correct. The real test checks the returned task has the expected title, status, and priority.
- **Database constraints**: Validation annotations (`@NotBlank`, `@Size`) only trigger with a real persistence layer. Mock tests skip validation entirely.
