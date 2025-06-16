package es.menasoft.juniemvc.repositories;

import es.menasoft.juniemvc.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testSaveCustomer() {
        Customer customer = Customer.builder()
                .name("Test Customer")
                .email("test@example.com")
                .phone("123-456-7890")
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isNotNull();
    }

    @Test
    void testGetCustomerById() {
        Customer customer = Customer.builder()
                .name("Test Customer")
                .email("test@example.com")
                .phone("123-456-7890")
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        Optional<Customer> fetchedCustomerOptional = customerRepository.findById(savedCustomer.getId());

        assertThat(fetchedCustomerOptional).isPresent();
        Customer fetchedCustomer = fetchedCustomerOptional.get();
        assertThat(fetchedCustomer.getName()).isEqualTo("Test Customer");
        assertThat(fetchedCustomer.getEmail()).isEqualTo("test@example.com");
        assertThat(fetchedCustomer.getPhone()).isEqualTo("123-456-7890");
    }

    @Test
    void testListCustomers() {
        Customer customer1 = Customer.builder()
                .name("Customer 1")
                .email("customer1@example.com")
                .phone("111-111-1111")
                .build();

        Customer customer2 = Customer.builder()
                .name("Customer 2")
                .email("customer2@example.com")
                .phone("222-222-2222")
                .build();

        customerRepository.save(customer1);
        customerRepository.save(customer2);

        List<Customer> customers = customerRepository.findAll();

        assertThat(customers).isNotNull();
        assertThat(customers.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    @Rollback
    void testUpdateCustomer() {
        Customer customer = Customer.builder()
                .name("Original Name")
                .email("original@example.com")
                .phone("123-456-7890")
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        
        savedCustomer.setName("Updated Name");
        savedCustomer.setEmail("updated@example.com");
        Customer updatedCustomer = customerRepository.save(savedCustomer);
        
        assertThat(updatedCustomer.getName()).isEqualTo("Updated Name");
        assertThat(updatedCustomer.getEmail()).isEqualTo("updated@example.com");
    }

    @Test
    @Rollback
    void testDeleteCustomer() {
        Customer customer = Customer.builder()
                .name("Delete Me")
                .email("delete@example.com")
                .phone("999-999-9999")
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        
        customerRepository.deleteById(savedCustomer.getId());
        
        Optional<Customer> deletedCustomer = customerRepository.findById(savedCustomer.getId());
        
        assertThat(deletedCustomer).isEmpty();
    }
}