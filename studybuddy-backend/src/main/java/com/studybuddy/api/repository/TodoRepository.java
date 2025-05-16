package com.studybuddy.api.repository;

import com.studybuddy.api.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByIsCompleted(boolean isCompleted);
}