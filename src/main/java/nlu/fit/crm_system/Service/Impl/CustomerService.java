package nlu.fit.crm_system.Service.Impl;

import nlu.fit.crm_system.DTO.request.UpdateCustomerRequest;
import nlu.fit.crm_system.Entities.Customer;
import nlu.fit.crm_system.Repositories.CustomerRepo;
import nlu.fit.crm_system.Service.Interfaces.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class CustomerService extends AService implements ICustomerService {
    @Autowired
    private CustomerRepo customerRepo;

    private static final Set<String> ALLOWED_STATUS = Set.of("LEAD", "POTENTIAL", "ACTIVE", "INACTIVE");

    CustomerService(){
        initialize();
    }

    private void initialize(){
        log.setName(this.getClass().getSimpleName());
        log.info("Initializing Customer Service");
    }

    @Override
    public Customer getCustomerById(String id) {
        if (id == null || id.isBlank()) {
            log.warn("getCustomerById", "Provided id is null or blank");
            throw new IllegalArgumentException("Invalid id");
        }
        Long parsedId;
        try {
            parsedId = Long.parseLong(id.trim());
        } catch (NumberFormatException ex) {
            log.warn("getCustomerById", "Invalid id format: " + id);
            throw new IllegalArgumentException("Invalid id format");
        }
        Optional<Customer> result = customerRepo.findById(parsedId);
        if (result.isEmpty()) {
            log.info("getCustomerById", "Customer not found for id=" + parsedId);
            throw new java.util.NoSuchElementException("Customer not found");
        }
        log.info("getCustomerById", "Customer found for id=" + parsedId);
        return result.get();
    }

    @Override
    public Customer updateCustomer(String id, UpdateCustomerRequest request) {
        if (id == null || id.isBlank()) {
            log.warn("updateCustomer", "Provided id is null or blank");
            throw new IllegalArgumentException("Invalid id");
        }
        Long parsedId;
        try {
            parsedId = Long.parseLong(id.trim());
        } catch (NumberFormatException ex) {
            log.warn("updateCustomer", "Invalid id format: " + id);
            throw new IllegalArgumentException("Invalid id format");
        }

        Customer existing = customerRepo.findById(parsedId)
                .orElseThrow(() -> new java.util.NoSuchElementException("Customer not found"));

        // Apply partial updates only for non-null fields
        if (request.getFirstName() != null) existing.setFirstName(request.getFirstName().trim());
        if (request.getLastName() != null) existing.setLastName(request.getLastName().trim());
        if (request.getEmail() != null) existing.setEmail(request.getEmail().trim());
        if (request.getPhone() != null) existing.setPhone(request.getPhone().trim());
        if (request.getCompany() != null) existing.setCompany(request.getCompany().trim());
        if (request.getStatus() != null) {
            String st = request.getStatus().trim();
            if (!ALLOWED_STATUS.contains(st)) {
                throw new IllegalArgumentException("Invalid status. Allowed: " + ALLOWED_STATUS);
            }
            existing.setStatus(st);
        }

        try {
            Customer saved = customerRepo.save(existing);
            log.info("updateCustomer", "Updated customer id=" + parsedId);
            return saved;
        } catch (DataIntegrityViolationException dive) {
            log.warn("updateCustomer", "Data integrity violation: " + dive.getMostSpecificCause().getMessage());
            // Rethrow to be handled as 409 Conflict by GlobalExceptionHandler (to be added)
            throw dive;
        }
    }
}
