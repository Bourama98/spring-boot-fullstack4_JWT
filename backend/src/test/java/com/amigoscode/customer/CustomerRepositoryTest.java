package com.amigoscode.customer;

import com.amigoscode.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)// To disable the Embedded database and use our own test container.
class CustomerRepositoryTest extends AbstractTestContainers {
    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsCustomerByEmail() {
        //GIVEN
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20,
                Gender.MALE);
        underTest.save(customer);
        //When
        var actual = underTest.existsCustomerByEmail(email);

        //Then

        assertThat(actual).isTrue();
    }
    @Test
    void existsCustomerByEmailFailsWhenEmailDoesNotExist() {
        //GIVEN
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        //When
        var actual = underTest.existsCustomerByEmail(email);

        //Then

        assertThat(actual).isFalse();
    }
    @Test
    void existsCustomerById() {
        //GIVEN
        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20,
                Gender.MALE);
        underTest.save(customer);
        int id = underTest.findAll()
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
    void existsCustomerByIdFailsWhenIdDoesNotExist() {
        //GIVEN

        int id = -1;
        //When
        var actual = underTest.existsCustomerById(id);

        //Then

        assertThat(actual).isFalse();
    }
}