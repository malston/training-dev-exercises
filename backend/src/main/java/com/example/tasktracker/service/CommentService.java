package com.example.tasktracker.service;

import com.example.tasktracker.model.Comment;
import com.example.tasktracker.repository.CommentRepository;
import com.example.tasktracker.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
    }

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
}
