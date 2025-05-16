package com.studybuddy.api.controller;

import com.studybuddy.api.dto.FlashcardDto;
import com.studybuddy.api.service.FlashcardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/flashcards")
public class FlashcardController {

    private final FlashcardService flashcardService;

    @Autowired
    public FlashcardController(FlashcardService flashcardService) {
        this.flashcardService = flashcardService;
    }

    @GetMapping
    public ResponseEntity<List<FlashcardDto>> getAllFlashcards() {
        return ResponseEntity.ok(flashcardService.getAllFlashcards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlashcardDto> getFlashcardById(@PathVariable Long id) {
        return ResponseEntity.ok(flashcardService.getFlashcardById(id));
    }

    @PostMapping
    public ResponseEntity<FlashcardDto> createFlashcard(@Valid @RequestBody FlashcardDto flashcardDto) {
        return new ResponseEntity<>(flashcardService.createFlashcard(flashcardDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlashcardDto> updateFlashcard(@PathVariable Long id, @Valid @RequestBody FlashcardDto flashcardDto) {
        return ResponseEntity.ok(flashcardService.updateFlashcard(id, flashcardDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlashcard(@PathVariable Long id) {
        flashcardService.deleteFlashcard(id);
        return ResponseEntity.noContent().build();
    }
}