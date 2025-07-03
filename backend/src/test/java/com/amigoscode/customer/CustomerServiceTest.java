package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.RequestValidationException;
import com.amigoscode.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)  // This is another way of initializing mock
class CustomerServiceTest {
    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {

        underTest = new CustomerService(customerDao);
    }


    @Test
    void getAllCustomers() {
       // When
        underTest.getAllCustomers();

        // Then

        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        //Given
        int customerId = 10;
        Customer customer = new Customer(
                customerId,
                "Alex",
                "Alex@gmail.com",
                19,
                Gender.MALE);
        when(customerDao.selectCustomerById(customerId))
                .thenReturn(
                Optional.of(customer));

        // When
         Customer actual = underTest.getCustomer(customerId);
        // Then
        assertThat(actual).isEqualTo(customer);
    }
    @Test
    void willThrowExceptionIfCustomerDoesNotExist() {
        //Given
        int customerId = 10;
        when(customerDao.selectCustomerById(customerId)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getCustomer(customerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(customerId)
                );

    }
    @Test
    void addCustomer() {
        //Given
        //int customerId = 10;
        String email = "Alex@gmail.com";
        when(customerDao.existsCustomerWithEmail(email)).thenReturn(false);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Alex",
                email,
                19,
                Gender.MALE
        );

        // When

        underTest.addCustomer(request);
        // Then
        ArgumentCaptor<Customer> requestCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).insertCustomer(requestCaptor.capture());
        Customer capturedRequest = requestCaptor.getValue();
        assertThat(capturedRequest.getId()).isNull();
        assertThat(capturedRequest.getName()).isEqualTo(request.name());
        assertThat(capturedRequest.getEmail()).isEqualTo(email);
        assertThat(capturedRequest.getAge()).isEqualTo(request.age());

    }
    @Test
    void willThrowWhenEmailExistWhileAddingCustomer() {
        //Given
        int customerId = 10;
        String email = "Alex@gmail.com";
        when(customerDao.existsCustomerWithEmail(email)).thenReturn(true);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Alex",
                email,
                19,
                Gender.MALE
        );

        // When

        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already exists");
        // Then

        verify(customerDao, never()).insertCustomer(any());


    }

    @Test
    void deleteCustomer() {
        //Given
        int customerId = 10;
        Customer customer = new Customer(
                customerId,
                "Alex",
                "Alex@gmail.com",
                19,
                Gender.MALE);

        when(customerDao.existsCustomerById(customerId)).thenReturn(true);
        // When
        underTest.deleteCustomer(customerId);

        // Then
        verify(customerDao).deleteCustomerById(customerId);
    }

    @Test
    void deleteWillThrowWhenIdDoesNotExistCustomer() {
        //Given
        int customerId = 10;

        when(customerDao.existsCustomerById(customerId)).thenReturn(false);
        // When


        // Then
        assertThatThrownBy(() -> underTest.deleteCustomer(customerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(customerId));

        verify(customerDao, never()).deleteCustomerById(customerId);
    }

    @Test
    void canUpdateAllCustomerProperties() {
        //Given
        int customerId = 10;

        Customer customer = new Customer(
                customerId,
                "Alex",
                "Alex@gmail.com",
                19,
                Gender.MALE);
        String email = "Alex2@gmail.com";
        when(customerDao.selectCustomerById(customerId))
                .thenReturn(
                        Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("Alexandro", email, 23);
        when(customerDao.existsCustomerWithEmail(email)).thenReturn(false);
        // When
      
        underTest.updateCustomer(customerId, updateRequest);

        // Then
        ArgumentCaptor<Customer> requestCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(requestCaptor.capture());
        Customer capturedRequest = requestCaptor.getValue();
        assertThat(capturedRequest.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedRequest.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedRequest.getAge()).isEqualTo(updateRequest.age());
    }
@Test
    void canUpdateOnlyCustomerName() {
        //Given
        int customerId = 10;
        Customer customer = new Customer(
                customerId,
                "Alex",
                "Alex@gmail.com",
                19,
                Gender.MALE);
        when(customerDao.selectCustomerById(customerId))
                .thenReturn(
                        Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("Alexandro", null, null);

        // When

        underTest.updateCustomer(customerId, updateRequest);

        // Then
        ArgumentCaptor<Customer> requestCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(requestCaptor.capture());
        Customer capturedRequest = requestCaptor.getValue();
        assertThat(capturedRequest.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedRequest.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedRequest.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        //Given
        int customerId = 10;

        Customer customer = new Customer(
                customerId,
                "Alex",
                "Alex@gmail.com",
                19,
                Gender.MALE);
        when(customerDao.selectCustomerById(customerId))
                .thenReturn(
                        Optional.of(customer));

        String newEmail = "Alexandro@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, newEmail, null);

        when(customerDao.existsCustomerWithEmail(newEmail)).thenReturn(false);
        // When

        underTest.updateCustomer(customerId, updateRequest);

        // Then
        ArgumentCaptor<Customer> requestCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(requestCaptor.capture());
        Customer capturedRequest = requestCaptor.getValue();

        assertThat(capturedRequest.getName()).isEqualTo(customer.getName());
        assertThat(capturedRequest.getEmail()).isEqualTo(newEmail);
        assertThat(capturedRequest.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateOnlyCustomerAge() {
        //Given
        int customerId = 10;
        Customer customer = new Customer(
                customerId,
                "Alex",
                "Alex@gmail.com",
                19,
                Gender.MALE);
        when(customerDao.selectCustomerById(customerId))
                .thenReturn(
                        Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, null, 38);

        // When

        underTest.updateCustomer(customerId, updateRequest);

        // Then
        ArgumentCaptor<Customer> requestCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(requestCaptor.capture());
        Customer capturedRequest = requestCaptor.getValue();

        assertThat(capturedRequest.getName()).isEqualTo(customer.getName());
        assertThat(capturedRequest.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedRequest.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void willThrowWhenTryingToUpdateCustomerEmailWhenAlreadyTaken() {
        //Given
        int customerId = 10;

        Customer customer = new Customer(
                customerId,
                "Alex",
                "Alex@gmail.com",
                19,
                Gender.MALE);
        when(customerDao.selectCustomerById(customerId))
                .thenReturn(
                        Optional.of(customer));

        String newEmail = "Alex1@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, newEmail, null);

        when(customerDao.existsCustomerWithEmail(newEmail)).thenReturn(true);
        // When

       assertThatThrownBy(() -> underTest.updateCustomer(customerId, updateRequest))
               .isInstanceOf(DuplicateResourceException.class)
                       .hasMessage("email already exists");

        // Then

        verify(customerDao, never()).updateCustomer(any());

    }

    @Test
    void willThrowWhenUpdateCustomerHasNoChanges() {
        //Given
        int customerId = 10;

        Customer customer = new Customer(
                customerId,
                "Alex",
                "Alex@gmail.com",
                19,
                Gender.MALE);
        when(customerDao.selectCustomerById(customerId))
                .thenReturn(
                        Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(customer.getName(), customer.getEmail(), customer.getAge());


        // When

        assertThatThrownBy(() -> underTest.updateCustomer(customerId, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("No change made to the data");

        // Then

        verify(customerDao, never()).updateCustomer(any());

    }
}