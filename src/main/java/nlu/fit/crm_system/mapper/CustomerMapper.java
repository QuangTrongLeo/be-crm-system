package nlu.fit.crm_system.mapper;

import nlu.fit.crm_system.DTO.response.CustomerResponse;
import nlu.fit.crm_system.Entities.Customer;

import java.util.List;

public class CustomerMapper {

    public static CustomerResponse toCustomerResponse(Customer customer) {
        if (customer == null) return null;

        return CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .company(customer.getCompany())
                .status(customer.getStatus())
                .assignedUserId(customer.getAssignedUser().getId())
                .build();
    }

    public static List<CustomerResponse> toCustomerResponseList(List<Customer> customers) {
        return customers.stream()
                .map(CustomerMapper::toCustomerResponse)
                .toList();
    }
}