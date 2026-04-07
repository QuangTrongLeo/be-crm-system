package nlu.fit.crm_system.Controller;

import jakarta.validation.Valid;
import nlu.fit.crm_system.DTO.request.CreateCustomerRequest;
import nlu.fit.crm_system.DTO.request.SearchRequest;
import nlu.fit.crm_system.DTO.request.UpdateCustomerRequest;
import nlu.fit.crm_system.DTO.response.CustomerResponse;
import nlu.fit.crm_system.DTO.response.ApiResponse;
import nlu.fit.crm_system.Service.Interfaces.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.crm-system-url}/customer")
public class CustomerController {

    @Autowired
    ICustomerService customerService;

    // ===== GET CUSTOMER BY ID =====
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(@PathVariable String id) {
        CustomerResponse customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(
                ApiResponse.<CustomerResponse>builder()
                        .code(200)
                        .message("Lấy thông tin khách hàng thành công")
                        .data(customer)
                        .build()
        );
    }

    // ===== GET ALL CUSTOMERS =====
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAllCustomers() {
        List<CustomerResponse> list = customerService.getAllCustomers();
        return ResponseEntity.ok(
                ApiResponse.<List<CustomerResponse>>builder()
                        .code(200)
                        .message("Lấy danh sách khách hàng thành công")
                        .data(list)
                        .build()
        );
    }

    // ===== CREATE CUSTOMER =====
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(
            @Valid @RequestBody CreateCustomerRequest request) {

        CustomerResponse created = customerService.createCustomer(request);
        return ResponseEntity.ok(
                ApiResponse.<CustomerResponse>builder()
                        .code(200)
                        .message("Tạo khách hàng thành công")
                        .data(created)
                        .build()
        );
    }

    // ===== UPDATE CUSTOMER =====
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
            @PathVariable String id,
            @Valid @RequestBody UpdateCustomerRequest request) {

        CustomerResponse updated = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(
                ApiResponse.<CustomerResponse>builder()
                        .code(200)
                        .message("Cập nhật khách hàng thành công")
                        .data(updated)
                        .build()
        );
    }

    // ===== DELETE CUSTOMER =====
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable String id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .message("Xóa khách hàng thành công")
                        .build()
        );
    }

    // ===== SEARCH CUSTOMER =====
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> searchCustomers(
            @Valid @RequestBody SearchRequest searchTerm) {

        List<CustomerResponse> list = customerService.searchFor(searchTerm);
        return ResponseEntity.ok(
                ApiResponse.<List<CustomerResponse>>builder()
                        .code(200)
                        .message("Tìm kiếm khách hàng thành công")
                        .data(list)
                        .build()
        );
    }
}