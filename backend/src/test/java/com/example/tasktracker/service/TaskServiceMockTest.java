package com.example.tasktracker.service;

import com.example.tasktracker.model.Task;
import com.example.tasktracker.model.TaskStatus;
import com.example.tasktracker.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * This test only verifies mock interactions, not real behavior.
 * Exercise 3 asks you to rewrite it using real dependencies.
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceMockTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void getAllTasksShouldCallFindAll() {
        List<Task> mockTasks = Arrays.asList(new Task("A", "B"), new Task("C", "D"));
        when(taskRepository.findAll()).thenReturn(mockTasks);

        List<Task> result = taskService.getAllTasks();

        assertThat(result).hasSize(2);
        verify(taskRepository).findAll();
    }

    @Test
    void createTaskShouldCallSave() {
        Task input = new Task("Test", "Description");
        when(taskRepository.save(any(Task.class))).thenReturn(input);

        Task result = taskService.createTask(input);

        assertThat(result.getTitle()).isEqualTo("Test");
        verify(taskRepository).save(input);
    }

    @Test
    void deleteTaskShouldCallDeleteWhenFound() {
        Task existing = new Task("Test", "Description");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));

        boolean result = taskService.deleteTask(1L);

        assertThat(result).isTrue();
        verify(taskRepository).delete(existing);
    }

    @Test
    void deleteTaskShouldReturnFalseWhenNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = taskService.deleteTask(1L);

        assertThat(result).isFalse();
        verify(taskRepository, never()).delete(any());
    }
}
