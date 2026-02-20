package com.example.tasktracker.service;

import com.example.tasktracker.model.Task;
import com.example.tasktracker.model.TaskPriority;
import com.example.tasktracker.model.TaskStatus;
import com.example.tasktracker.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TaskStatisticsServiceTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatisticsService statisticsService;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();

        Task t1 = new Task("Task A", "Description A");
        t1.setStatus(TaskStatus.TODO);
        t1.setPriority(TaskPriority.HIGH);
        taskRepository.save(t1);

        Task t2 = new Task("Task B", "Description B");
        t2.setStatus(TaskStatus.IN_PROGRESS);
        t2.setPriority(TaskPriority.MEDIUM);
        taskRepository.save(t2);

        Task t3 = new Task("Task C", "Description C");
        t3.setStatus(TaskStatus.DONE);
        t3.setPriority(TaskPriority.LOW);
        taskRepository.save(t3);

        Task t4 = new Task("Task D", "Description D");
        t4.setStatus(TaskStatus.TODO);
        t4.setPriority(TaskPriority.HIGH);
        taskRepository.save(t4);
    }

    @Test
    void countByStatusShouldReturnCorrectCounts() {
        Map<TaskStatus, Long> counts = statisticsService.countByStatus();

        assertThat(counts.get(TaskStatus.TODO)).isEqualTo(2);
        assertThat(counts.get(TaskStatus.IN_PROGRESS)).isEqualTo(1);
        assertThat(counts.get(TaskStatus.DONE)).isEqualTo(1);
    }

    @Test
    void completionRateShouldCalculatePercentage() {
        // 1 out of 4 tasks is DONE = 25%
        double rate = statisticsService.completionRate();
        assertThat(rate).isEqualTo(25.0);
    }

    @Test
    void completionRateShouldReturnZeroWhenNoTasks() {
        taskRepository.deleteAll();
        double rate = statisticsService.completionRate();
        assertThat(rate).isEqualTo(0.0);
    }

    @Test
    void countByPriorityShouldReturnCorrectCounts() {
        Map<TaskPriority, Long> counts = statisticsService.countByPriority();

        assertThat(counts.get(TaskPriority.HIGH)).isEqualTo(2);
        assertThat(counts.get(TaskPriority.MEDIUM)).isEqualTo(1);
        assertThat(counts.get(TaskPriority.LOW)).isEqualTo(1);
    }
}
