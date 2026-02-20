package com.example.tasktracker.service;

import com.example.tasktracker.model.Task;
import com.example.tasktracker.model.TaskPriority;
import com.example.tasktracker.model.TaskStatus;
import com.example.tasktracker.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskStatisticsService {

    private final TaskRepository taskRepository;

    public TaskStatisticsService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Map<TaskStatus, Long> countByStatus() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));
    }

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

    public Map<TaskPriority, Long> countByPriority() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .collect(Collectors.groupingBy(Task::getPriority, Collectors.counting()));
    }
}
