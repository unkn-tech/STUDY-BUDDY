package com.studybuddy.api.service.impl;

import com.studybuddy.api.dto.ChatMessageDto;
import com.studybuddy.api.model.ChatMessage;
import com.studybuddy.api.repository.ChatMessageRepository;
import com.studybuddy.api.service.ChatbotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatbotServiceImpl implements ChatbotService {

    private static final Logger logger = LoggerFactory.getLogger(ChatbotServiceImpl.class);

    private final ChatMessageRepository chatMessageRepository;

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Value("${openai.model}")
    private String openaiModel;

    @Autowired
    public ChatbotServiceImpl(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public ChatMessageDto sendMessage(String message, String conversationId) {
        ChatMessage userMessage = new ChatMessage();
        userMessage.setRole("user");
        userMessage.setContent(message);
        userMessage.setConversationId(conversationId);
        chatMessageRepository.save(userMessage);

        List<ChatMessage> conversationHistory = chatMessageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
        String aiResponse = callOpenAiApi(conversationHistory);

        ChatMessage aiMessage = new ChatMessage();
        aiMessage.setRole("assistant");
        aiMessage.setContent(aiResponse);
        aiMessage.setConversationId(conversationId);
        ChatMessage savedAiMessage = chatMessageRepository.save(aiMessage);

        return mapToDto(savedAiMessage);
    }

    private String callOpenAiApi(List<ChatMessage> conversationHistory) {
        try {
            if (openaiApiKey == null || openaiApiKey.isEmpty()) {
                logger.warn("OpenAI API key is not set. Using fallback mode.");
                return getFallbackResponse(conversationHistory);
            }

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + openaiApiKey);

            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", "You are a helpful study assistant called StudyBuddy. " +
                    "You help students with their studies, provide learning tips, explain concepts, " +
                    "and offer motivation. Keep responses concise, friendly, and educational. " +
                    "If asked about topics outside of education or studying, politely redirect the conversation " +
                    "back to educational topics.");
            messages.add(systemMessage);

            int historySize = conversationHistory.size();
            int startIndex = Math.max(0, historySize - 10);

            for (int i = startIndex; i < historySize; i++) {
                ChatMessage msg = conversationHistory.get(i);
                Map<String, String> messageMap = new HashMap<>();
                messageMap.put("role", msg.getRole());
                messageMap.put("content", msg.getContent());
                messages.add(messageMap);
            }

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", openaiModel);
            requestBody.put("messages", messages);
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 500);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            logger.info("Sending request to OpenAI with model: {}", openaiModel);
            logger.info("Number of messages in request: {}", messages.size());

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.openai.com/v1/chat/completions",
                    entity,
                    Map.class
            );

            logger.info("OpenAI API response status: {}", response.getStatusCode());

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    Map<String, Object> message = (Map<String, Object>) choice.get("message");
                    String content = (String) message.get("content");
                    logger.info("Received response from OpenAI");
                    return content;
                }
            }

            logger.warn("Unexpected response format from OpenAI API");
            return "I'm sorry, I couldn't process your request at the moment.";
        } catch (RestClientException e) {
            logger.error("Error calling OpenAI API: {}", e.getMessage(), e);
            return "I apologize, but I encountered an error while connecting to my knowledge base. Please try again later.";
        } catch (Exception e) {
            logger.error("Unexpected error in callOpenAiApi: ", e);
            return "I apologize, but I encountered an error while processing your request. Please try again later.";
        }
    }

    @Override
    public List<ChatMessageDto> getConversationHistory(String conversationId) {
        return chatMessageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public String createNewConversation() {
        return UUID.randomUUID().toString();
    }

    private ChatMessageDto mapToDto(ChatMessage chatMessage) {
        ChatMessageDto dto = new ChatMessageDto();
        dto.setId(chatMessage.getId());
        dto.setRole(chatMessage.getRole());
        dto.setContent(chatMessage.getContent());
        dto.setCreatedAt(chatMessage.getCreatedAt());
        dto.setConversationId(chatMessage.getConversationId());
        return dto;
    }

    private String getFallbackResponse(List<ChatMessage> conversationHistory) {
        String lastUserMessage = "";
        for (int i = conversationHistory.size() - 1; i >= 0; i--) {
            ChatMessage msg = conversationHistory.get(i);
            if ("user".equals(msg.getRole())) {
                lastUserMessage = msg.getContent().toLowerCase();
                break;
            }
        }

        if (lastUserMessage.contains("hello") || lastUserMessage.contains("hi ") || lastUserMessage.contains("hey")) {
            return "Hello! I'm StudyBuddy, your learning assistant. How can I help you with your studies today?";
        } else if (lastUserMessage.contains("how are you")) {
            return "I'm just a program, but I'm ready to help you with your studies! What would you like assistance with?";
        } else if (lastUserMessage.contains("thank")) {
            return "You're welcome! Feel free to ask if you need more help with your studies.";
        } else if (lastUserMessage.contains("bye") || lastUserMessage.contains("goodbye")) {
            return "Goodbye! Good luck with your studies. Come back anytime you need help!";
        } else if (lastUserMessage.contains("pomodoro")) {
            return "The Pomodoro Technique is a time management method where you work for 25 minutes, then take a 5-minute break. After 4 cycles, take a longer break of 15-30 minutes.";
        } else if (lastUserMessage.contains("focus") || lastUserMessage.contains("concentrate")) {
            return "To improve focus: 1) Remove distractions, 2) Use the Pomodoro Technique, 3) Take short breaks, 4) Stay hydrated, 5) Get enough sleep.";
        } else if (lastUserMessage.contains("study plan") || lastUserMessage.contains("exam")) {
            return "For an effective study plan: 1) Start early, 2) Break material into chunks, 3) Use active recall, 4) Practice tests, 5) Rest well.";
        } else if (lastUserMessage.contains("note") || lastUserMessage.contains("taking notes")) {
            return "Try the Cornell method, use abbreviations, review notes within 24 hours, and create mind maps.";
        } else if (lastUserMessage.contains("motivat")) {
            return "Try small goals, rewards, a study buddy, a fresh environment, and remind yourself of your long-term goals.";
        } else if (lastUserMessage.contains("memory") || lastUserMessage.contains("remember")) {
            return "Use spaced repetition, mnemonics, teach others, sleep well, and engage multiple senses.";
        }

        return "I'm in offline mode with limited help. Please check your API configuration. I can still offer basic study tips!";
    }
}
