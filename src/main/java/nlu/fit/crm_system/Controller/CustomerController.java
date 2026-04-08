package nlu.fit.crm_system.Controller;

import jakarta.validation.Valid;
import nlu.fit.crm_system.DTO.request.SearchRequest;
import nlu.fit.crm_system.DTO.request.UpdateCustomerRequest;
import nlu.fit.crm_system.DTO.request.CreateCustomerRequest;
import nlu.fit.crm_system.DTO.response.ApiResponse;
import nlu.fit.crm_system.Entities.Customer;
import nlu.fit.crm_system.Service.Interfaces.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.crm-system-url}/customer/")
public class CustomerController {
    @Autowired
    ICustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        Customer created = customerService.createCustomer(request);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String id){
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String id, @Valid @RequestBody UpdateCustomerRequest request) {
        Customer updated = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("search")
    public ResponseEntity<List<Customer>> getAllCustomers(@Valid @RequestBody SearchRequest searchTerm) {
        var list = customerService.searchFor(searchTerm);
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCustomer(@PathVariable String id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .code(200)
                        .message("Xóa khách hàng thành công")
                        .data("Deleted customer with id = " + id)
                        .build()
        );
    }
}
