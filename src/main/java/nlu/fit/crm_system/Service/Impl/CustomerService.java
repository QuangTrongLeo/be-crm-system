package nlu.fit.crm_system.Service.Impl;

import nlu.fit.crm_system.DTO.request.CreateCustomerRequest;
import nlu.fit.crm_system.DTO.request.UpdateCustomerRequest;
import nlu.fit.crm_system.Entities.Customer;
import nlu.fit.crm_system.Repositories.CustomerRepo;
import nlu.fit.crm_system.Service.Interfaces.ICustomerService;
import nlu.fit.crm_system.Utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import nlu.fit.crm_system.mapper.CustomerMapper;
import nlu.fit.crm_system.DTO.response.CustomerResponse;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service

public class CustomerService extends AService implements ICustomerService {
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private JwtUtils jwtUtils;

    private static final Set<String> ALLOWED_STATUS = Set.of("LEAD", "POTENTIAL", "ACTIVE", "INACTIVE");

    CustomerService(){
        initialize();
    }

    private void initialize(){
        log.setName(this.getClass().getSimpleName());
        log.info("Initializing Customer Service");
    }

    @Override
    public CustomerResponse getCustomerById(String id) {
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
        return CustomerMapper.toCustomerResponse(result.get());
    }

    @Override
    public CustomerResponse updateCustomer(String id, UpdateCustomerRequest request) {
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
            return CustomerMapper.toCustomerResponse(saved);
        } catch (DataIntegrityViolationException dive) {
            log.warn("updateCustomer",
                    "Data integrity violation: " + dive.getMostSpecificCause().getMessage());
            throw dive;
        }
    }

//    @Override
//    public List<CustomerResponse> searchFor(SearchRequest searchTerm) {
//        if (searchTerm == null) {
//            log.error("searchFor", "Provided searchTerm is null or blank");
//            throw new IllegalArgumentException("Invalid searchTerm");
//        }
//
//        var user_id = jwtUtils.getCurrentUser().getId();
//        var assignedCustomers = getAssignedCustomers(user_id);
//        var searchText = searchTerm.getSearchTerm() != null ? searchTerm.getSearchTerm().trim().toLowerCase() : "";
//        var filter = searchTerm.getFilter();
//        assignedCustomers = filterCustomers(assignedCustomers, filter);
//
//        List<Customer> result;
//
//        if (searchText.contains("@")) {
//            log.info("Searching for customer by email");
//            result = assignedCustomers.stream()
//                    .filter(c -> c.getEmail() != null &&
//                            c.getEmail().toLowerCase().contains(searchText))
//                    .toList();
//        }
//
//        else if (searchText.matches("[0-9]")) {
//            log.info("Searching for customer by phone");
//            result = assignedCustomers.stream()
//                    .filter(c -> c.getPhone() != null &&
//                            c.getPhone().contains(searchText))
//                    .toList();
//        }
//
//        else {
//            log.info("Searching for customer by name");
//            result = assignedCustomers.stream()
//                    .filter(c -> (c.getFirstName() != null &&
//                            c.getFirstName().toLowerCase().contains(searchText)) ||
//                            (c.getLastName() != null &&
//                                    c.getLastName().toLowerCase().contains(searchText)))
//                    .toList();
//        }
//
//        return CustomerMapper.toCustomerResponseList(result);
//    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        List<Customer> customers = customerRepo.findAll();
        log.info("getAllCustomers", "Fetched all customers");
        return CustomerMapper.toCustomerResponseList(customers);
    }

    @Override
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request is null");
        }
        String status = request.getStatus() != null ? request.getStatus().trim().toUpperCase() : "LEAD";
        if (!ALLOWED_STATUS.contains(status)) {
            throw new IllegalArgumentException("Invalid status. Allowed: " + ALLOWED_STATUS);
        }
        var user = jwtUtils.getCurrentUser();
        Customer customer = Customer.builder()
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .email(request.getEmail() != null ? request.getEmail().trim() : null)
                .phone(request.getPhone() != null ? request.getPhone().trim() : null)
                .company(request.getCompany() != null ? request.getCompany().trim() : null)
                .status(status)
                .assignedUser(user)
                .build();
        Customer saved = customerRepo.save(customer);
        log.info("createCustomer", "Created customer id=" + saved.getId());
        return CustomerMapper.toCustomerResponse(saved);
    }

    @Override
    public void deleteCustomer(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Invalid id");
        }
        Long parsedId;
        try {
            parsedId = Long.parseLong(id.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid id format");
        }
        Customer customer = customerRepo.findById(parsedId).orElseThrow(() -> new RuntimeException("Customer not found"));
        customerRepo.delete(customer);
        log.info("deleteCustomer", "Deleted customer id=" + parsedId);
    }


    @Override
    public List<CustomerResponse> search(String keyword) {
        String k = keyword == null ? "" : keyword.trim().toLowerCase();
        List<Customer> result = customerRepo.findAll().stream()
                .filter(c -> matchCustomer(c, k))
                .toList();
        return CustomerMapper.toCustomerResponseList(result);
    }

    private boolean matchCustomer(Customer c, String keyword) {
        if (keyword == null || keyword.isBlank()) return true;

        String k = keyword.toLowerCase();

        return (c.getFirstName() != null &&
                c.getFirstName().toLowerCase().contains(k))

                || (c.getLastName() != null &&
                c.getLastName().toLowerCase().contains(k))

                || (c.getEmail() != null &&
                c.getEmail().toLowerCase().contains(k))

                || (c.getPhone() != null &&
                c.getPhone().contains(keyword))

                || (c.getCompany() != null &&
                c.getCompany().toLowerCase().contains(k));
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

//    private List<Customer> getAssignedCustomers(Long userId) {
//        return customerRepo.findAll().stream()
//                .filter(c -> c.getAssignedUserId() != null &&
//                        c.getAssignedUserId().equals(userId)).toList();
//    }

}
