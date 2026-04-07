package nlu.fit.crm_system.Service.Impl;

import lombok.RequiredArgsConstructor;
import nlu.fit.crm_system.DTO.request.NoteRequest;
import nlu.fit.crm_system.DTO.response.NoteResponse;
import nlu.fit.crm_system.Entities.Customer;
import nlu.fit.crm_system.Entities.Note;
import nlu.fit.crm_system.Entities.User;
import nlu.fit.crm_system.Repositories.CustomerRepo;
import nlu.fit.crm_system.Repositories.NoteRepo;
import nlu.fit.crm_system.Repositories.UserRepo;
import nlu.fit.crm_system.Service.Interfaces.INoteService;
import nlu.fit.crm_system.Utils.JwtUtils;
import nlu.fit.crm_system.mapper.NoteMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService implements INoteService {

    private final JwtUtils jwtUtils;
    private final NoteRepo noteRepo;
    private final CustomerRepo customerRepo;

    @Override
    @Transactional
    public NoteResponse createNote(NoteRequest request) {
        User currentUser = jwtUtils.getCurrentUser();

        Customer customer = customerRepo.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        Note note = Note.builder()
                .customer(customer)
                .user(currentUser)
                .content(request.getContent())
                .isImportant(request.isImportant())
                .build();

        return NoteMapper.toNoteResponse(noteRepo.save(note));
    }

    @Override
    @Transactional
    public NoteResponse updateNote(Long id, NoteRequest request) {
        User currentUser = jwtUtils.getCurrentUser();
        Note note = noteRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ghi chú"));

        if (!note.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Bạn không có quyền chỉnh sửa ghi chú này!");
        }

        note.setContent(request.getContent());
        note.setImportant(request.isImportant());

        return NoteMapper.toNoteResponse(noteRepo.save(note));
    }

    @Override
    @Transactional
    public void deleteNote(Long id) {
        User currentUser = jwtUtils.getCurrentUser();
        Note note = noteRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ghi chú với ID: " + id));
        if (!note.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Bạn không có quyền xóa ghi chú của người khác!");
        }
        noteRepo.delete(note);
    }

    @Override
    public NoteResponse getNoteById(Long id) {
        Note note = noteRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ghi chú với ID: " + id));
        return NoteMapper.toNoteResponse(note);
    }

    @Override
    public List<NoteResponse> getAllNotes() {
        List<Note> notes = noteRepo.findAll();
        return NoteMapper.toNoteResponseList(notes);
    }

    @Override
    public List<NoteResponse> getNotesByCustomer(Long customerId) {
        // Kiểm tra xem khách hàng có tồn tại không
        if (!customerRepo.existsById(customerId)) {
            throw new RuntimeException("Khách hàng không tồn tại");
        }

        List<Note> notes = noteRepo.findByCustomerIdOrderByCreatedAtDesc(customerId);
        return NoteMapper.toNoteResponseList(notes);
    }
}