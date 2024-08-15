package com.codegym.bai_1.service;

import com.codegym.bai_1.model.Customer;

import java.util.List;

public interface ICustomerService {
    List<Customer> findAll();

    Customer findById(Long id);

    void save(Customer customer);

    void remove(Long id);
}
