package nlu.fit.crm_system.Service.Impl;

import nlu.fit.crm_system.DTO.request.SearchRequest;
import nlu.fit.crm_system.DTO.request.UpdateCustomerRequest;
import nlu.fit.crm_system.DTO.request.CreateCustomerRequest;
import nlu.fit.crm_system.Entities.Customer;
import nlu.fit.crm_system.Repositories.CustomerRepo;
import nlu.fit.crm_system.Service.Interfaces.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
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
    public Customer createCustomer(CreateCustomerRequest request) {
        if (request == null) {
            log.error("createCustomer", "Request is null");
            throw new IllegalArgumentException("Invalid request");
        }

        Customer customer = Customer.builder()
                .firstName(request.getFirstName() != null ? request.getFirstName().trim() : null)
                .lastName(request.getLastName() != null ? request.getLastName().trim() : null)
                .email(request.getEmail() != null ? request.getEmail().trim() : null)
                .phone(request.getPhone() != null ? request.getPhone().trim() : null)
                .company(request.getCompany() != null ? request.getCompany().trim() : null)
                .assignedUserId(request.getAssignedUserId())
                .build();

        if (request.getStatus() != null) {
            String st = request.getStatus().trim();
            if (!ALLOWED_STATUS.contains(st)) {
                throw new IllegalArgumentException("Invalid status. Allowed: " + ALLOWED_STATUS);
            }
            customer.setStatus(st);
        } else {
            customer.setStatus("LEAD"); // Default status, optional
        }

        try {
            Customer saved = customerRepo.save(customer);
            log.info("createCustomer", "Created customer with id=" + saved.getId());
            return saved;
        } catch (DataIntegrityViolationException dive) {
            log.warn("createCustomer", "Data integrity violation: " + dive.getMostSpecificCause().getMessage());
            throw dive;
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        log.info("getAllCustomers", "Fetching all customers");
        return customerRepo.findAll();
    }

    @Override
    public void deleteCustomer(String id) {
        if (id == null || id.isBlank()) {
            log.warn("deleteCustomer", "Provided id is null or blank");
            throw new IllegalArgumentException("Invalid id");
        }

        Long parsedId;
        try {
            parsedId = Long.parseLong(id.trim());
        } catch (NumberFormatException ex) {
            log.warn("deleteCustomer", "Invalid id format: " + id);
            throw new IllegalArgumentException("Invalid id format");
        }

        Customer existing = customerRepo.findById(parsedId)
                .orElseThrow(() -> new java.util.NoSuchElementException("Customer not found"));

        customerRepo.delete(existing);
        log.info("deleteCustomer", "Deleted customer id=" + parsedId);
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

    private List<Customer> getAssignedCustomers(Long userId) {
        return customerRepo.findAll().stream()
                .filter(c -> c.getAssignedUserId() != null &&
                        c.getAssignedUserId().equals(userId)).toList();
    }

    @Override
    public List<Customer> searchFor(SearchRequest searchTerm) {
        if (searchTerm == null) {
            log.error("searchFor", "Provided searchTerm is null or blank");
            throw new IllegalArgumentException("Invalid searchTerm");
        }

        var user_id = searchTerm.getUser_id();
        var assignedCustomers = getAssignedCustomers(user_id);
        var searchText = searchTerm.getSearchTerm().toLowerCase();
        var filter = searchTerm.getFilter();
        assignedCustomers = filterCustomers(assignedCustomers, filter);

        if (searchText.contains("@")) {
            log.info("Searching for customer by email");
            return assignedCustomers.stream()
                    .filter(c -> c.getEmail() != null &&
                            c.getEmail().contains(searchText))
                    .toList();
        }

        if (searchText.matches("[0-9]")) {
            log.info("Searching for customer by phone");
            return assignedCustomers.stream()
                    .filter(c -> c.getPhone() != null &&
                            c.getPhone().contains(searchText))
                    .toList();
        }

        log.info("Searching for customer by name");
        return assignedCustomers.stream()
                .filter(c -> (c.getFirstName() != null &&
                        c.getFirstName().toLowerCase().contains(searchText)) ||
                        (c.getLastName() != null &&
                                c.getLastName().toLowerCase().contains(searchText)))
                .toList();

    }



    private List<Customer> filterCustomers(List<Customer> customers, String filter) {
        if (filter == null || filter.isBlank()) {
            return customers;
        }
        log.info("Filtering customers by filter: " + filter);
        String f = filter.trim().toUpperCase();
        if (!ALLOWED_STATUS.contains(f)) {
            log.warn("filterCustomers", "Invalid filter value: " + filter);
            throw new IllegalArgumentException("Invalid filter value. Allowed: " + ALLOWED_STATUS);
        }
        return customers.stream()
                .filter(c -> c.getStatus() != null &&
                        c.getStatus().equalsIgnoreCase(f))
                .toList();
    }

}
