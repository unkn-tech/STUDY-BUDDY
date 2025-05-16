package com.studybuddy.api.service.impl;

import com.studybuddy.api.dto.FlashcardDto;
import com.studybuddy.api.model.Flashcard;
import com.studybuddy.api.repository.FlashcardRepository;
import com.studybuddy.api.service.FlashcardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlashcardServiceImpl implements FlashcardService {

    private final FlashcardRepository flashcardRepository;

    @Autowired
    public FlashcardServiceImpl(FlashcardRepository flashcardRepository) {
        this.flashcardRepository = flashcardRepository;
    }

    @Override
    public List<FlashcardDto> getAllFlashcards() {
        return flashcardRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public FlashcardDto getFlashcardById(Long id) {
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found with id: " + id));
        return mapToDto(flashcard);
    }

    @Override
    public FlashcardDto createFlashcard(FlashcardDto flashcardDto) {
        Flashcard flashcard = mapToEntity(flashcardDto);
        Flashcard savedFlashcard = flashcardRepository.save(flashcard);
        return mapToDto(savedFlashcard);
    }

    @Override
    public FlashcardDto updateFlashcard(Long id, FlashcardDto flashcardDto) {
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found with id: " + id));
        
        flashcard.setQuestion(flashcardDto.getQuestion());
        flashcard.setAnswer(flashcardDto.getAnswer());
        
        Flashcard updatedFlashcard = flashcardRepository.save(flashcard);
        return mapToDto(updatedFlashcard);
    }

    @Override
    public void deleteFlashcard(Long id) {
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found with id: " + id));
        flashcardRepository.delete(flashcard);
    }
    
    private FlashcardDto mapToDto(Flashcard flashcard) {
        FlashcardDto flashcardDto = new FlashcardDto();
        flashcardDto.setId(flashcard.getId());
        flashcardDto.setQuestion(flashcard.getQuestion());
        flashcardDto.setAnswer(flashcard.getAnswer());
        flashcardDto.setCreatedAt(flashcard.getCreatedAt());
        flashcardDto.setUpdatedAt(flashcard.getUpdatedAt());
        return flashcardDto;
    }
    
    private Flashcard mapToEntity(FlashcardDto flashcardDto) {
        Flashcard flashcard = new Flashcard();
        flashcard.setQuestion(flashcardDto.getQuestion());
        flashcard.setAnswer(flashcardDto.getAnswer());
        
        // Only set ID if it's an update operation
        if (flashcardDto.getId() != null) {
            flashcard.setId(flashcardDto.getId());
        }
        
        return flashcard;
    }
}