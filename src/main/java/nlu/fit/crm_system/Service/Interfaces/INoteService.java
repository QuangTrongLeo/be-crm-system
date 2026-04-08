package nlu.fit.crm_system.Service.Interfaces;

import nlu.fit.crm_system.DTO.request.CreateNoteRequest;
import nlu.fit.crm_system.Entities.Note;
import java.util.List;

public interface INoteService {
    Note addNote(CreateNoteRequest request);
    List<Note> getNotesForCustomer(Long customerId);
    Note markAsImportant(Long noteId, boolean isImportant);
}

