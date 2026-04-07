package nlu.fit.crm_system.Service.Interfaces;

import nlu.fit.crm_system.DTO.request.NoteRequest;
import nlu.fit.crm_system.DTO.response.NoteResponse;

import java.util.List;

public interface INoteService {
    NoteResponse createNote(NoteRequest request);
    NoteResponse updateNote(Long id, NoteRequest request);
    void deleteNote(Long id);
    NoteResponse getNoteById(Long id);
    List<NoteResponse> getAllNotes();
    List<NoteResponse> getNotesByCustomer(Long customerId);
}
