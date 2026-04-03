package nlu.fit.crm_system.Controller;

import nlu.fit.crm_system.Entities.Customer;
import nlu.fit.crm_system.Service.Interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer/")
public class CustomerController {
    @Autowired
    IUserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getUser(@PathVariable String id){

        return null;
    }
}
