package nlu.fit.crm_system.Service.Interfaces;

import nlu.fit.crm_system.DTO.request.UpdateCustomerRequest;
import nlu.fit.crm_system.Entities.Customer;
import org.springframework.stereotype.Service;

@Service
public interface ICustomerService {
    Customer getCustomerById(String id);
    Customer updateCustomer(String id, UpdateCustomerRequest request);
}
