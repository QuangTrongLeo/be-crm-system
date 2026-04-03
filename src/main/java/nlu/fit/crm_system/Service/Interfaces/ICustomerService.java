package nlu.fit.crm_system.Service.Interfaces;

import nlu.fit.crm_system.Entities.Customer;
import org.springframework.stereotype.Service;

@Service
public interface ICustomerService {
    public Customer getCustomerById(String id);
    public void UpdateCustomer(Customer customer);
}
