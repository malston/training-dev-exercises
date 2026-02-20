package com.example.tasktracker.controller;

import com.example.tasktracker.model.Task;
import com.example.tasktracker.model.TaskPriority;
import com.example.tasktracker.model.TaskStatus;
import com.example.tasktracker.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerSearchTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();

        Task task1 = new Task("Set up CI pipeline", "Configure GitHub Actions");
        task1.setStatus(TaskStatus.TODO);
        task1.setPriority(TaskPriority.HIGH);
        taskRepository.save(task1);

        Task task2 = new Task("Fix CI permissions", "Pipeline fails on fork PRs");
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2.setPriority(TaskPriority.MEDIUM);
        taskRepository.save(task2);

        Task task3 = new Task("Add user authentication", "JWT-based auth");
        task3.setStatus(TaskStatus.TODO);
        task3.setPriority(TaskPriority.HIGH);
        taskRepository.save(task3);
    }

    @Test
    void searchShouldFindTasksBySubstring() throws Exception {
        // "CI" should match both "Set up CI pipeline" and "Fix CI permissions"
        mockMvc.perform(get("/api/tasks/search").param("q", "CI"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void searchShouldBeCaseInsensitive() throws Exception {
        // "ci" (lowercase) should still match "CI" in task titles
        mockMvc.perform(get("/api/tasks/search").param("q", "ci"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void searchShouldReturnEmptyForNoMatch() throws Exception {
        mockMvc.perform(get("/api/tasks/search").param("q", "nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
