package com.studybuddy.api.service;

import com.studybuddy.api.dto.NoteDto;

import java.util.List;

public interface NoteService {
    
    List<NoteDto> getAllNotes();
    
    NoteDto getNoteById(Long id);
    
    NoteDto createNote(NoteDto noteDto);
    
    NoteDto updateNote(Long id, NoteDto noteDto);
    
    void deleteNote(Long id);
}