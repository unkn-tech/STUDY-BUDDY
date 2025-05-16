package com.studybuddy.api.controller;

import com.studybuddy.api.dto.NoteDto;
import com.studybuddy.api.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public ResponseEntity<List<NoteDto>> getAllNotes() {
        return ResponseEntity.ok(noteService.getAllNotes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteDto> getNoteById(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.getNoteById(id));
    }

    @PostMapping
    public ResponseEntity<NoteDto> createNote(@Valid @RequestBody NoteDto noteDto) {
        return new ResponseEntity<>(noteService.createNote(noteDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteDto> updateNote(@PathVariable Long id, @Valid @RequestBody NoteDto noteDto) {
        return ResponseEntity.ok(noteService.updateNote(id, noteDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}