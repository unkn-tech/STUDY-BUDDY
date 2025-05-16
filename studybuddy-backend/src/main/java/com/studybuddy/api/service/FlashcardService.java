package com.studybuddy.api.service;

import com.studybuddy.api.dto.FlashcardDto;

import java.util.List;

public interface FlashcardService {
    
    List<FlashcardDto> getAllFlashcards();
    
    FlashcardDto getFlashcardById(Long id);
    
    FlashcardDto createFlashcard(FlashcardDto flashcardDto);
    
    FlashcardDto updateFlashcard(Long id, FlashcardDto flashcardDto);
    
    void deleteFlashcard(Long id);
}