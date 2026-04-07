package nlu.fit.crm_system.mapper;

import nlu.fit.crm_system.DTO.response.NoteResponse;
import nlu.fit.crm_system.Entities.Note;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class NoteMapper {

    // Ánh xạ đơn lẻ
    public static NoteResponse toNoteResponse(Note note) {
        if (note == null) return null;

        return NoteResponse.builder()
                .id(note.getId())
                .customerId(note.getCustomer().getId())
                .customerName(note.getCustomer().getFirstName() + " " + note.getCustomer().getLastName())
                .userId(note.getUser().getId())
                .userName(note.getUser().getFullName())
                .content(note.getContent())
                .isImportant(note.isImportant())
                .createdAt(note.getCreatedAt())
                .build();
    }

    public static List<NoteResponse> toNoteResponseList(List<Note> notes) {
        if (notes == null || notes.isEmpty()) {
            return Collections.emptyList();
        }
        return notes.stream()
                .map(NoteMapper::toNoteResponse)
                .toList();
    }
}
