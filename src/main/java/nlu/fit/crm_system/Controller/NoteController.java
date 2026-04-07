package nlu.fit.crm_system.Controller;

import lombok.RequiredArgsConstructor;
import nlu.fit.crm_system.DTO.request.NoteRequest;
import nlu.fit.crm_system.DTO.response.ApiResponse;
import nlu.fit.crm_system.DTO.response.NoteResponse;
import nlu.fit.crm_system.Service.Interfaces.INoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.crm-system-url}/notes")
@RequiredArgsConstructor
public class NoteController {

    private final INoteService noteService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
    @PostMapping
    public ResponseEntity<ApiResponse<NoteResponse>> createNote(@RequestBody NoteRequest request) {
        NoteResponse data = noteService.createNote(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<NoteResponse>builder()
                        .code(200)
                        .message("Tạo ghi chú thành công")
                        .data(data)
                        .build()
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NoteResponse>> updateNote(
            @PathVariable Long id,
            @RequestBody NoteRequest request) {
        NoteResponse data = noteService.updateNote(id, request);
        return ResponseEntity.ok(
                ApiResponse.<NoteResponse>builder()
                        .code(200)
                        .message("Cập nhật ghi chú thành công")
                        .data(data)
                        .build()
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .message("Xóa ghi chú thành công")
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NoteResponse>> getNoteById(@PathVariable Long id) {
        NoteResponse data = noteService.getNoteById(id);
        return ResponseEntity.ok(
                ApiResponse.<NoteResponse>builder()
                        .code(200)
                        .message("Lấy thông tin ghi chú thành công")
                        .data(data)
                        .build()
        );
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<NoteResponse>>> getAllNotes() {
        List<NoteResponse> data = noteService.getAllNotes();
        return ResponseEntity.ok(
                ApiResponse.<List<NoteResponse>>builder()
                        .code(200)
                        .message("Lấy tất cả ghi chú thành công")
                        .data(data)
                        .build()
        );
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<NoteResponse>>> getNotesByCustomer(@PathVariable Long customerId) {
        List<NoteResponse> data = noteService.getNotesByCustomer(customerId);
        return ResponseEntity.ok(
                ApiResponse.<List<NoteResponse>>builder()
                        .code(200)
                        .message("Lấy danh sách ghi chú khách hàng thành công")
                        .data(data)
                        .build()
        );
    }
}