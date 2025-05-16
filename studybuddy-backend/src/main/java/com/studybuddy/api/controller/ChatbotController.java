package com.studybuddy.api.controller;

import com.studybuddy.api.dto.ChatMessageDto;
import com.studybuddy.api.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chatbot")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @Autowired
    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping("/send")
    public ResponseEntity<ChatMessageDto> sendMessage(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        String conversationId = request.get("conversationId");
        
        if (conversationId == null || conversationId.isEmpty()) {
            conversationId = chatbotService.createNewConversation();
        }
        
        return ResponseEntity.ok(chatbotService.sendMessage(message, conversationId));
    }

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<ChatMessageDto>> getConversationHistory(@PathVariable String conversationId) {
        return ResponseEntity.ok(chatbotService.getConversationHistory(conversationId));
    }

    @PostMapping("/conversation/new")
    public ResponseEntity<Map<String, String>> createNewConversation() {
        String conversationId = chatbotService.createNewConversation();
        return ResponseEntity.ok(Map.of("conversationId", conversationId));
    }
}