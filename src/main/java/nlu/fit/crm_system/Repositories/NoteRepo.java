package nlu.fit.crm_system.Repositories;

import nlu.fit.crm_system.Entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepo extends JpaRepository<Note, Long> {
    List<Note> findByCustomerId(Long customerId);
}

