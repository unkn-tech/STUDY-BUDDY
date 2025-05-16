package com.studybuddy.api.repository;

import com.studybuddy.api.model.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {
    // Custom query methods can be added here if needed
}