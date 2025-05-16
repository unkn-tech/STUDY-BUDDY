package com.studybuddy.api.repository;

import com.studybuddy.api.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    List<ChatMessage> findByConversationIdOrderByCreatedAtAsc(String conversationId);
}