package nlu.fit.crm_system.Controller;

import jakarta.validation.Valid;
import nlu.fit.crm_system.DTO.request.CreateNoteRequest;
import nlu.fit.crm_system.DTO.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<Note>> addNote(@Valid @RequestBody CreateNoteRequest request) {
        Note created = noteService.addNote(request);
        return ResponseEntity.status(201).body(
            ApiResponse.<Note>builder()
                .code(201)
                .message("Thêm ghi chú thành công")
                .data(created)
                .build()
        );
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<Note>>> getNotesForCustomer(@PathVariable Long customerId) {
        List<Note> notes = noteService.getNotesForCustomer(customerId);
        return ResponseEntity.ok(
            ApiResponse.<List<Note>>builder()
                .code(200)
                .message("Lấy danh sách ghi chú thành công")
                .data(notes)
                .build()
        );
    }

    @PatchMapping("/{noteId}/important")
    public ResponseEntity<ApiResponse<Note>> markAsImportant(@PathVariable Long noteId, @RequestParam boolean isImportant) {
        Note updated = noteService.markAsImportant(noteId, isImportant);
        return ResponseEntity.ok(
            ApiResponse.<Note>builder()
                .code(200)
                .message("Cập nhật trạng thái ghi chú thành công")
                .data(updated)
                .build()
        );
    }
}
