package com.studybuddy.api.service.impl;

import com.studybuddy.api.dto.TodoDto;
import com.studybuddy.api.model.Todo;
import com.studybuddy.api.repository.TodoRepository;
import com.studybuddy.api.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public List<TodoDto> getAllTodos() {
        return todoRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDto> getCompletedTodos() {
        return todoRepository.findByIsCompleted(true).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDto> getIncompleteTodos() {
        return todoRepository.findByIsCompleted(false).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TodoDto getTodoById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
        return mapToDto(todo);
    }

    @Override
    public TodoDto createTodo(TodoDto todoDto) {
        Todo todo = mapToEntity(todoDto);
        Todo savedTodo = todoRepository.save(todo);
        return mapToDto(savedTodo);
    }

    @Override
    public TodoDto updateTodo(Long id, TodoDto todoDto) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
        
        todo.setText(todoDto.getText());
        todo.setCompleted(todoDto.isCompleted());
        
        Todo updatedTodo = todoRepository.save(todo);
        return mapToDto(updatedTodo);
    }

    @Override
    public TodoDto toggleTodoStatus(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
        
        todo.setCompleted(!todo.isCompleted());
        Todo updatedTodo = todoRepository.save(todo);
        return mapToDto(updatedTodo);
    }

    @Override
    public void deleteTodo(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
        todoRepository.delete(todo);
    }
    
    private TodoDto mapToDto(Todo todo) {
        TodoDto todoDto = new TodoDto();
        todoDto.setId(todo.getId());
        todoDto.setText(todo.getText());
        todoDto.setCompleted(todo.isCompleted());
        todoDto.setCreatedAt(todo.getCreatedAt());
        todoDto.setUpdatedAt(todo.getUpdatedAt());
        return todoDto;
    }
    
    private Todo mapToEntity(TodoDto todoDto) {
        Todo todo = new Todo();
        todo.setText(todoDto.getText());
        todo.setCompleted(todoDto.isCompleted());
        
        // Only set ID if it's an update operation
        if (todoDto.getId() != null) {
            todo.setId(todoDto.getId());
        }
        
        return todo;
    }
}