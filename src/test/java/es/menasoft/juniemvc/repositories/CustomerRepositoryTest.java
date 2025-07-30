package es.menasoft.juniemvc.repositories;

import es.menasoft.juniemvc.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testSaveCustomer() {
        Customer customer = Customer.builder()
                .name("Test Customer")
                .email("test@example.com")
                .phone("123-456-7890")
                .addressLine1("123 Main St")
                .addressLine2("Apt 4B")
                .city("Anytown")
                .state("CA")
                .postalCode("12345")
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(savedCustomer.getAddressLine1()).isEqualTo("123 Main St");
        assertThat(savedCustomer.getAddressLine2()).isEqualTo("Apt 4B");
        assertThat(savedCustomer.getCity()).isEqualTo("Anytown");
        assertThat(savedCustomer.getState()).isEqualTo("CA");
        assertThat(savedCustomer.getPostalCode()).isEqualTo("12345");
    }

    @Test
    void testGetCustomerById() {
        Customer customer = Customer.builder()
                .name("Test Customer")
                .email("test@example.com")
                .phone("123-456-7890")
                .addressLine1("123 Main St")
                .addressLine2("Apt 4B")
                .city("Anytown")
                .state("CA")
                .postalCode("12345")
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        Optional<Customer> fetchedCustomerOptional = customerRepository.findById(savedCustomer.getId());

        assertThat(fetchedCustomerOptional).isPresent();
        Customer fetchedCustomer = fetchedCustomerOptional.get();
        assertThat(fetchedCustomer.getName()).isEqualTo("Test Customer");
        assertThat(fetchedCustomer.getEmail()).isEqualTo("test@example.com");
        assertThat(fetchedCustomer.getPhone()).isEqualTo("123-456-7890");
        assertThat(fetchedCustomer.getAddressLine1()).isEqualTo("123 Main St");
        assertThat(fetchedCustomer.getAddressLine2()).isEqualTo("Apt 4B");
        assertThat(fetchedCustomer.getCity()).isEqualTo("Anytown");
        assertThat(fetchedCustomer.getState()).isEqualTo("CA");
        assertThat(fetchedCustomer.getPostalCode()).isEqualTo("12345");
    }

    @Test
    void testListCustomers() {
        Customer customer1 = Customer.builder()
                .name("Customer 1")
                .email("customer1@example.com")
                .phone("111-111-1111")
                .addressLine1("123 First St")
                .city("Anytown")
                .state("CA")
                .postalCode("12345")
                .build();

        Customer customer2 = Customer.builder()
                .name("Customer 2")
                .email("customer2@example.com")
                .phone("222-222-2222")
                .addressLine1("456 Second St")
                .city("Othertown")
                .state("NY")
                .postalCode("67890")
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
                .addressLine1("123 Old St")
                .addressLine2("Apt 1A")
                .city("Oldtown")
                .state("CA")
                .postalCode("12345")
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        
        savedCustomer.setName("Updated Name");
        savedCustomer.setEmail("updated@example.com");
        savedCustomer.setAddressLine1("456 New St");
        savedCustomer.setAddressLine2("Suite 2B");
        savedCustomer.setCity("Newtown");
        savedCustomer.setState("NY");
        savedCustomer.setPostalCode("67890");
        Customer updatedCustomer = customerRepository.save(savedCustomer);
        
        assertThat(updatedCustomer.getName()).isEqualTo("Updated Name");
        assertThat(updatedCustomer.getEmail()).isEqualTo("updated@example.com");
        assertThat(updatedCustomer.getAddressLine1()).isEqualTo("456 New St");
        assertThat(updatedCustomer.getAddressLine2()).isEqualTo("Suite 2B");
        assertThat(updatedCustomer.getCity()).isEqualTo("Newtown");
        assertThat(updatedCustomer.getState()).isEqualTo("NY");
        assertThat(updatedCustomer.getPostalCode()).isEqualTo("67890");
    }

    @Test
    @Rollback
    void testDeleteCustomer() {
        Customer customer = Customer.builder()
                .name("Delete Me")
                .email("delete@example.com")
                .phone("999-999-9999")
                .addressLine1("999 Delete St")
                .city("Deletetown")
                .state("CA")
                .postalCode("99999")
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        
        customerRepository.deleteById(savedCustomer.getId());
        
        Optional<Customer> deletedCustomer = customerRepository.findById(savedCustomer.getId());
        
        assertThat(deletedCustomer).isEmpty();
    }
}