package com.amigoscode.customer;

import com.amigoscode.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class CustomerJDBCDataAccessServiceTest extends AbstractTestContainers {

    private CustomerJDBCDataAccessService underTest;
    private CustomerRowMapper customerRowMapper = new CustomerRowMapper();


    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        //GIVEN
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().fullName();
        Customer actual = new Customer(
                name,
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(actual);
        //WHEN
        List<Customer> actualCustomer = underTest.selectAllCustomers();
        System.out.println(actualCustomer);
        assertThat(actualCustomer).isNotEmpty();

    }

    @Test
    void selectCustomerById() {
        //GIVEN
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then

        assertThat(actual).isPresent().hasValueSatisfying(c ->{
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo(customer.getName());
                    assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                    assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
        System.out.println(actual);
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        //Given
        int id = -1;
        // When
        var actual = underTest.selectCustomerById(id);
        // Then
        assertThat(actual).isEmpty();
        System.out.println(actual);
    }

    @Test
    void insertCustomer() {
        //GIVEN
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20,
                Gender.MALE);
        //When
        underTest.insertCustomer(customer);

        //Then
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        var actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(
                c ->{
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo(customer.getName());
                    assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                    assertThat(c.getAge()).isEqualTo(customer.getAge());
                }
        );

    }

    @Test
    void existsCustomerWithEmail() {
        //Given
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);
        //When
        boolean actual = underTest.existsCustomerWithEmail(email);
        //Then

        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerWithEmailReturnsFalseWhenDoesNotExists() {
        //Given
String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        // When
boolean actual = underTest.existsCustomerWithEmail(email);
        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomerById() {
        //Given
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20,
                Gender.MALE);

        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //When
        underTest.deleteCustomerById(id);


        //Then
       var actual = underTest.selectCustomerById(id);
       assertThat(actual).isNotPresent();
    }

    @Test
    void existsCustomerById() {
        //Given
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //When

        var actual = underTest.existsCustomerById(id);

        //Then

        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerWithIdWillReturnFalseWhenIdNotPresent() {
        //Given
        int id = -1;
        // When
        var actual = underTest.existsCustomerById(id);
        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void updateCustomerName() {
        //Given
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //When
        var newName = "Foo";
        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);
        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName);
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
                }
        );
    }

    @Test
    void updateCustomerEmail() {
        //Given
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        var newEmail = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer update = new Customer();
        update.setId(id);
        update.setEmail(newEmail);
         underTest.updateCustomer(update);
        // Then
        var actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getEmail()).isEqualTo(newEmail);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }

    @Test
    void updateCustomerAge() {
        //Given
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        int newAge = 200;
        Customer update = new Customer();
        update.setId(id);
        update.setAge(newAge);
        underTest.updateCustomer(update);

        // Then
        var actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(newAge);
        });
    }

    @Test
    void updateAllPropertiesOfCustomer() {
        //Given
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        var newName = faker.name().fullName()+"New";
        var newEmail = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        int newAge = 180;

        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);
        update.setEmail(newEmail);
        update.setAge(newAge);
        update.setGender(Gender.MALE);
        underTest.updateCustomer(update);

        // Then
        var actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName);
            assertThat(c.getEmail()).isEqualTo(newEmail);
            assertThat(c.getAge()).isEqualTo(newAge);
            assertThat(c.getGender()).isEqualTo(Gender.MALE);
        });

    }

    @Test
    void noCustomerPropertiesUpdated() {
        //Given
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20,
                Gender.MALE);
        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        Customer update = new Customer();
        update.setId(id);
        underTest.updateCustomer(update);

        // Then
        var actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }
}