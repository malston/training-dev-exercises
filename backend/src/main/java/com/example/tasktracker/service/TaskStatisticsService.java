package com.example.tasktracker.service;

import com.example.tasktracker.model.TaskPriority;
import com.example.tasktracker.model.TaskStatus;
import com.example.tasktracker.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TaskStatisticsService {

    private final TaskRepository taskRepository;

    public TaskStatisticsService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Map<TaskStatus, Long> countByStatus() {
        throw new UnsupportedOperationException("Not yet implemented -- TDD exercise");
    }

    public double completionRate() {
        throw new UnsupportedOperationException("Not yet implemented -- TDD exercise");
    }

    public Map<TaskPriority, Long> countByPriority() {
        throw new UnsupportedOperationException("Not yet implemented -- TDD exercise");
    }
}
