package com.amigoscode.customer;

import com.amigoscode.AbstractTestContainers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest extends AbstractTestContainers {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
       autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }
    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }


    @Test
    void selectAllCustomers() {
        // When
        underTest.selectAllCustomers();

        // Then
        verify(customerRepository).findAll();
        //Mockito.verify(customerRepository, Mockito.times(1)).findAll();
    }

    @Test
    void selectCustomerById() {
        //Given
        int id = 1;

        // When
       underTest.selectCustomerById(id);
        // Then
        verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        //Given
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().fullName();
        Customer actual = new Customer(
                name,
                email,
                20,
                Gender.MALE);
        // When
        underTest.insertCustomer(actual);

        // Then
        verify(customerRepository).save(actual);
    }

    @Test
    void existsCustomerWithEmail() {
        //Given
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        // When
        underTest.existsCustomerWithEmail(email);

        // Then
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void deleteCustomerById() {
        //Given
        int id = 1;

        // When
        underTest.deleteCustomerById(id);

        // Then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void existsCustomerById() {
        //Given
        int id = 1;
        // When
        underTest.existsCustomerById(id);

        // Then
        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void updateCustomer() {
        //Given
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().fullName();
        Customer actual = new Customer(
                name,
                email,
                20,
                Gender.MALE);

        // When
        underTest.updateCustomer(actual);

        // Then
        verify(customerRepository).save(actual);
    }
}