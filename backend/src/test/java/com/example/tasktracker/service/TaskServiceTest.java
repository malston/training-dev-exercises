package com.example.tasktracker.service;

import com.example.tasktracker.model.Task;
import com.example.tasktracker.model.TaskPriority;
import com.example.tasktracker.model.TaskStatus;
import com.example.tasktracker.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void getAllTasksShouldReturnAllSavedTasks() {
        taskRepository.save(new Task("Task A", "Description A"));
        taskRepository.save(new Task("Task B", "Description B"));

        List<Task> result = taskService.getAllTasks();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Task A", "Task B");
    }

    @Test
    void createTaskShouldPersistAndReturnTask() {
        Task input = new Task("Test Task", "Test Description");

        Task result = taskService.createTask(input);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Task");
        assertThat(taskRepository.findById(result.getId())).isPresent();
    }

    @Test
    void deleteTaskShouldRemoveFromDatabase() {
        Task saved = taskRepository.save(new Task("To Delete", "Will be removed"));
        Long id = saved.getId();

        boolean result = taskService.deleteTask(id);

        assertThat(result).isTrue();
        assertThat(taskRepository.findById(id)).isEmpty();
    }

    @Test
    void deleteTaskShouldReturnFalseForNonexistentId() {
        boolean result = taskService.deleteTask(99999L);

        assertThat(result).isFalse();
    }

    @Test
    void updateTaskShouldModifyExistingTask() {
        Task saved = taskRepository.save(new Task("Original", "Original desc"));

        Task updates = new Task("Updated", "Updated desc");
        updates.setStatus(TaskStatus.DONE);
        updates.setPriority(TaskPriority.HIGH);

        Optional<Task> result = taskService.updateTask(saved.getId(), updates);

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Updated");
        assertThat(result.get().getStatus()).isEqualTo(TaskStatus.DONE);
        assertThat(result.get().getPriority()).isEqualTo(TaskPriority.HIGH);
    }

    @Test
    void updateTaskShouldReturnEmptyForNonexistentId() {
        Task updates = new Task("Updated", "Updated desc");

        Optional<Task> result = taskService.updateTask(99999L, updates);

        assertThat(result).isEmpty();
    }
}
