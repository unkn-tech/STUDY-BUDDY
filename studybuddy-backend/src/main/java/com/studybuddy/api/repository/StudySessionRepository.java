package com.studybuddy.api.repository;

import com.studybuddy.api.model.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StudySessionRepository extends JpaRepository<StudySession, Long> {
    
    List<StudySession> findBySessionDateBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT SUM(s.durationMinutes) FROM StudySession s")
    Integer getTotalStudyMinutes();
}