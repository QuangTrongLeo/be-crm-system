package nlu.fit.crm_system.Controller;

import jakarta.validation.Valid;
import nlu.fit.crm_system.DTO.request.CreateNoteRequest;
import nlu.fit.crm_system.Entities.Note;
import nlu.fit.crm_system.Service.Interfaces.INoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.crm-system-url}/notes")
public class NoteController {
    @Autowired
    private INoteService noteService;

    @PostMapping
    public ResponseEntity<Note> addNote(@Valid @RequestBody CreateNoteRequest request) {
        Note created = noteService.addNote(request);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Note>> getNotesForCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(noteService.getNotesForCustomer(customerId));
    }

    @PatchMapping("/{noteId}/important")
    public ResponseEntity<Note> markAsImportant(@PathVariable Long noteId, @RequestParam boolean isImportant) {
        return ResponseEntity.ok(noteService.markAsImportant(noteId, isImportant));
    }
}

