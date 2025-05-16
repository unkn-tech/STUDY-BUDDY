package com.studybuddy.api.service;

import com.studybuddy.api.dto.TodoDto;

import java.util.List;

public interface TodoService {
    
    List<TodoDto> getAllTodos();
    
    List<TodoDto> getCompletedTodos();
    
    List<TodoDto> getIncompleteTodos();
    
    TodoDto getTodoById(Long id);
    
    TodoDto createTodo(TodoDto todoDto);
    
    TodoDto updateTodo(Long id, TodoDto todoDto);
    
    TodoDto toggleTodoStatus(Long id);
    
    void deleteTodo(Long id);
}