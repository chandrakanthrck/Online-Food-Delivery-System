package com.fooddelivery.service;

import com.fooddelivery.entity.Customer;
import com.fooddelivery.repository.CustomerRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    public Customer createCustomer(String name, String address){
        Customer customer = new Customer();
        customer.setName(name);
        customer.setAddress(address);
        return customerRepository.save(customer);
    }

    public Optional<Customer> getCustomerById(Long id){
        return customerRepository.findById(id);
    }
}
