package com.studybuddy.api.repository;

import com.studybuddy.api.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    // Custom query methods can be added here if needed
}