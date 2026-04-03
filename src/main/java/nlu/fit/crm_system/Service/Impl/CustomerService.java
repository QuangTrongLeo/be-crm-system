package nlu.fit.crm_system.Service.Impl;

import nlu.fit.crm_system.Entities.Customer;
import nlu.fit.crm_system.Repositories.CustomerRepo;
import nlu.fit.crm_system.Service.Interfaces.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService extends AService implements ICustomerService {
    @Autowired
    private CustomerRepo customerRepo;

    CustomerService(){
        initialize();
    }

    private void initialize(){
        log.setName(this.getClass().getSimpleName());
        log.info("Initializing Customer Service");
    }

    @Override
    public Customer getCustomerById(String id) {
        return null;
    }

    @Override
    public void UpdateCustomer(Customer customer) {

    }
}
