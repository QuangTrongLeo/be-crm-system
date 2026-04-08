package nlu.fit.crm_system.Service.Impl;

import nlu.fit.crm_system.DTO.request.CreateNoteRequest;
import nlu.fit.crm_system.Entities.Customer;
import nlu.fit.crm_system.Entities.Note;
import nlu.fit.crm_system.Entities.User;
import nlu.fit.crm_system.Repositories.CustomerRepo;
import nlu.fit.crm_system.Repositories.NoteRepo;
import nlu.fit.crm_system.Repositories.UserRepository;
import nlu.fit.crm_system.Service.Interfaces.INoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteService implements INoteService {
    @Autowired
    private NoteRepo noteRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Note addNote(CreateNoteRequest request) {
        Customer customer = customerRepo.findById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Note note = Note.builder()
                .customer(customer)
                .user(user)
                .content(request.getContent())
                .isImportant(request.getIsImportant() != null ? request.getIsImportant() : false)
                .createdAt(LocalDateTime.now())
                .build();

        return noteRepo.save(note);
    }

    @Override
    public List<Note> getNotesForCustomer(Long customerId) {
        return noteRepo.findByCustomerId(customerId);
    }

    @Override
    public Note markAsImportant(Long noteId, boolean isImportant) {
        Note note = noteRepo.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("Note not found"));
        note.setIsImportant(isImportant);
        return noteRepo.save(note);
    }
}

