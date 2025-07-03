package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.RequestValidationException;
import com.amigoscode.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers(){
     return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id){
        return customerDao.selectCustomerById(id)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("customer with id [%s] not found".formatted(id)
                        ));
    }
    public void addCustomer(
            CustomerRegistrationRequest customerRegistrationRequest ){
       // Check if email exists
        if(customerDao.existsCustomerWithEmail(customerRegistrationRequest.email())){
            throw new DuplicateResourceException("email already exists");
        }
        // add
        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age(),
                customerRegistrationRequest.gender() );
        customerDao.insertCustomer(customer);
    }
    public void deleteCustomer(Integer id){
        if(!customerDao.existsCustomerById(id)){
            throw new ResourceNotFoundException("customer with id [%s] not found".formatted(id));
        }
        customerDao.deleteCustomerById(id);
    }
    public void updateCustomer(Integer id, CustomerUpdateRequest customerUpdateRequest){
        Customer customer = getCustomer(id);
        boolean changed = false;

        if(customerUpdateRequest.name() != null && !customerUpdateRequest.name().equals(customer.getName())){
            customer.setName(customerUpdateRequest.name());
            changed = true;

        }

        if (customerUpdateRequest.age() !=  null && !customerUpdateRequest.age().equals(customer.getAge())){

            customer.setAge(customerUpdateRequest.age());
            changed = true;
        }
        if (customerUpdateRequest.email() != null && !customerUpdateRequest.email().equals(customer.getEmail())){
            if (customerDao.existsCustomerWithEmail(customerUpdateRequest.email())) {
                throw new DuplicateResourceException("email already exists");
            }
            customer.setEmail(customerUpdateRequest.email());
            changed = true;
        }
        if(!changed){
            throw new RequestValidationException("No change made to the data");
        }

        customerDao.updateCustomer(customer);
    }

}
