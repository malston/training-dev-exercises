package com.example.tasktracker.controller;

import com.example.tasktracker.model.Comment;
import com.example.tasktracker.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
@CrossOrigin(origins = "http://localhost:3000")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long taskId) {
        return ResponseEntity.ok(commentService.getComments(taskId));
    }

    @PostMapping
    public ResponseEntity<Comment> addComment(
            @PathVariable Long taskId,
            @Valid @RequestBody Comment comment) {
        try {
            Comment created = commentService.addComment(taskId, comment);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
