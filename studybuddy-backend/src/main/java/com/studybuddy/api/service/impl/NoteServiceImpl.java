package com.studybuddy.api.service.impl;

import com.studybuddy.api.dto.NoteDto;
import com.studybuddy.api.model.Note;
import com.studybuddy.api.repository.NoteRepository;
import com.studybuddy.api.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public List<NoteDto> getAllNotes() {
        return noteRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public NoteDto getNoteById(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found with id: " + id));
        return mapToDto(note);
    }

    @Override
    public NoteDto createNote(NoteDto noteDto) {
        Note note = mapToEntity(noteDto);
        Note savedNote = noteRepository.save(note);
        return mapToDto(savedNote);
    }

    @Override
    public NoteDto updateNote(Long id, NoteDto noteDto) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found with id: " + id));
        
        note.setTitle(noteDto.getTitle());
        note.setContent(noteDto.getContent());
        
        Note updatedNote = noteRepository.save(note);
        return mapToDto(updatedNote);
    }

    @Override
    public void deleteNote(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found with id: " + id));
        noteRepository.delete(note);
    }
    
    private NoteDto mapToDto(Note note) {
        NoteDto noteDto = new NoteDto();
        noteDto.setId(note.getId());
        noteDto.setTitle(note.getTitle());
        noteDto.setContent(note.getContent());
        noteDto.setCreatedAt(note.getCreatedAt());
        noteDto.setUpdatedAt(note.getUpdatedAt());
        return noteDto;
    }
    
    private Note mapToEntity(NoteDto noteDto) {
        Note note = new Note();
        note.setTitle(noteDto.getTitle());
        note.setContent(noteDto.getContent());
        
        // Only set ID if it's an update operation
        if (noteDto.getId() != null) {
            note.setId(noteDto.getId());
        }
        
        return note;
    }
}