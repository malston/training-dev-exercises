package com.example.tasktracker.controller;

import com.example.tasktracker.model.Task;
import com.example.tasktracker.model.TaskPriority;
import com.example.tasktracker.model.TaskStatus;
import com.example.tasktracker.repository.CommentRepository;
import com.example.tasktracker.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long taskId;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
        taskRepository.deleteAll();

        Task task = new Task("Test task", "For comment testing");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.MEDIUM);
        task = taskRepository.save(task);
        taskId = task.getId();
    }

    @Test
    void getCommentsShouldReturnEmptyListForNewTask() throws Exception {
        mockMvc.perform(get("/api/tasks/{taskId}/comments", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void addCommentShouldCreateAndReturnComment() throws Exception {
        String body = objectMapper.writeValueAsString(
                Map.of("author", "Alice", "content", "Looking into this"));

        mockMvc.perform(post("/api/tasks/{taskId}/comments", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.author").value("Alice"))
                .andExpect(jsonPath("$.content").value("Looking into this"))
                .andExpect(jsonPath("$.taskId").value(taskId))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void getCommentsShouldReturnAddedComments() throws Exception {
        String comment1 = objectMapper.writeValueAsString(
                Map.of("author", "Alice", "content", "First comment"));
        String comment2 = objectMapper.writeValueAsString(
                Map.of("author", "Bob", "content", "Second comment"));

        mockMvc.perform(post("/api/tasks/{taskId}/comments", taskId)
                .contentType(MediaType.APPLICATION_JSON).content(comment1));
        mockMvc.perform(post("/api/tasks/{taskId}/comments", taskId)
                .contentType(MediaType.APPLICATION_JSON).content(comment2));

        mockMvc.perform(get("/api/tasks/{taskId}/comments", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].author").value("Alice"))
                .andExpect(jsonPath("$[1].author").value("Bob"));
    }

    @Test
    void addCommentToNonexistentTaskShouldReturn400() throws Exception {
        String body = objectMapper.writeValueAsString(
                Map.of("author", "Alice", "content", "Orphan comment"));

        mockMvc.perform(post("/api/tasks/{taskId}/comments", 99999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}
