package com.studybuddy.api.controller;

import com.studybuddy.api.dto.TodoDto;
import com.studybuddy.api.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public ResponseEntity<List<TodoDto>> getAllTodos() {
        return ResponseEntity.ok(todoService.getAllTodos());
    }

    @GetMapping("/completed")
    public ResponseEntity<List<TodoDto>> getCompletedTodos() {
        return ResponseEntity.ok(todoService.getCompletedTodos());
    }

    @GetMapping("/incomplete")
    public ResponseEntity<List<TodoDto>> getIncompleteTodos() {
        return ResponseEntity.ok(todoService.getIncompleteTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoDto> getTodoById(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.getTodoById(id));
    }

    @PostMapping
    public ResponseEntity<TodoDto> createTodo(@Valid @RequestBody TodoDto todoDto) {
        return new ResponseEntity<>(todoService.createTodo(todoDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoDto> updateTodo(@PathVariable Long id, @Valid @RequestBody TodoDto todoDto) {
        return ResponseEntity.ok(todoService.updateTodo(id, todoDto));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TodoDto> toggleTodoStatus(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.toggleTodoStatus(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }
}