package com.amigoscode.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{
    private static List<Customer> customers;
    static {
        customers = new ArrayList<>();
        Customer alex = new Customer(
                1,
                "Alex",
                "alex@gamil.com",
                19,
                Gender.MALE);
        Customer mangara = new Customer(
                2,
                "MANGARA",
                "mangara@gamil.com",
                29,
                Gender.MALE);
        customers.add(alex);
        customers.add(mangara);
    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customers.stream()
                .filter(c->c.getId().equals(id))
                .findFirst();

    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        return customers.stream()
                .anyMatch(c->c.getEmail().equals(email));
    }

    @Override
    public void deleteCustomerById(Integer id) {
        customers.stream().filter(
                c->c.getId().equals(id)).findFirst()
                .ifPresent(customers::remove) ;

    }

    @Override
    public boolean existsCustomerById(Integer id) {

        return customers.stream().anyMatch(c->c.getId().equals(id));
    }

    @Override
    public void updateCustomer(Customer customer) {
     customers.add(customer);
    }
}
