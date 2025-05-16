package com.studybuddy.api.service;

import com.studybuddy.api.dto.ChatMessageDto;

import java.util.List;

public interface ChatbotService {
    
    ChatMessageDto sendMessage(String message, String conversationId);
    
    List<ChatMessageDto> getConversationHistory(String conversationId);
    
    String createNewConversation();
}