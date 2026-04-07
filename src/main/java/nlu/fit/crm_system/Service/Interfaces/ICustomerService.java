package nlu.fit.crm_system.Service.Interfaces;

import nlu.fit.crm_system.DTO.request.CreateCustomerRequest;
import nlu.fit.crm_system.DTO.request.SearchRequest;
import nlu.fit.crm_system.DTO.request.UpdateCustomerRequest;
import nlu.fit.crm_system.DTO.response.CustomerResponse;
import nlu.fit.crm_system.Entities.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICustomerService {
    CustomerResponse getCustomerById(String id);
    CustomerResponse updateCustomer(String id, UpdateCustomerRequest request);
    List<CustomerResponse> searchFor(SearchRequest searchTerm);
    List<CustomerResponse> getAllCustomers();
    CustomerResponse createCustomer(CreateCustomerRequest request);
    void deleteCustomer(String id);
}
