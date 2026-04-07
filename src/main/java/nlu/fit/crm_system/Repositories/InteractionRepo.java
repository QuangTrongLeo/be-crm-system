package nlu.fit.crm_system.Repositories;

import nlu.fit.crm_system.Entities.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InteractionRepo extends JpaRepository<Interaction, Long> {
    List<Interaction> findByCustomerIdOrderByInteractionDateDesc(Long customerId);
    List<Interaction> findAllByOrderByInteractionDateDesc();
}
