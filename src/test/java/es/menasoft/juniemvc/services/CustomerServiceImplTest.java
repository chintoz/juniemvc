package es.menasoft.juniemvc.services;

import es.menasoft.juniemvc.entities.Customer;
import es.menasoft.juniemvc.mappers.CustomerMapper;
import es.menasoft.juniemvc.models.CustomerDto;
import es.menasoft.juniemvc.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer testCustomer;
    private CustomerDto testCustomerDto;
    private List<Customer> testCustomerList;
    private List<CustomerDto> testCustomerDtoList;

    @BeforeEach
    void setUp() {
        // Setup test Customer entity
        testCustomer = Customer.builder()
                .id(1)
                .name("Test Customer")
                .email("test@example.com")
                .phone("123-456-7890")
                .build();

        Customer testCustomer2 = Customer.builder()
                .id(2)
                .name("Another Customer")
                .email("another@example.com")
                .phone("987-654-3210")
                .build();

        testCustomerList = Arrays.asList(testCustomer, testCustomer2);

        // Setup test CustomerDto
        testCustomerDto = new CustomerDto(
                1,
                null,
                "Test Customer",
                "test@example.com",
                "123-456-7890",
                null,
                null
        );

        CustomerDto testCustomerDto2 = new CustomerDto(
                2,
                null,
                "Another Customer",
                "another@example.com",
                "987-654-3210",
                null,
                null
        );

        testCustomerDtoList = Arrays.asList(testCustomerDto, testCustomerDto2);
    }

    @Test
    void saveCustomer() {
        // Given
        CustomerDto customerDtoToSave = new CustomerDto(
                null,
                null,
                "New Customer",
                "new@example.com",
                "555-555-5555",
                null,
                null
        );

        Customer customerToSave = Customer.builder()
                .name("New Customer")
                .email("new@example.com")
                .phone("555-555-5555")
                .build();

        Customer savedCustomer = Customer.builder()
                .id(3)
                .name("New Customer")
                .email("new@example.com")
                .phone("555-555-5555")
                .build();

        CustomerDto savedCustomerDto = new CustomerDto(
                3,
                null,
                "New Customer",
                "new@example.com",
                "555-555-5555",
                null,
                null
        );

        when(customerMapper.customerDtoToCustomer(customerDtoToSave)).thenReturn(customerToSave);
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);
        when(customerMapper.customerToCustomerDto(savedCustomer)).thenReturn(savedCustomerDto);

        // When
        CustomerDto result = customerService.saveCustomer(customerDtoToSave);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(3);
        assertThat(result.name()).isEqualTo("New Customer");
        verify(customerMapper).customerDtoToCustomer(customerDtoToSave);
        verify(customerRepository).save(any(Customer.class));
        verify(customerMapper).customerToCustomerDto(savedCustomer);
    }

    @Test
    void getCustomerById() {
        // Given
        when(customerRepository.findById(1)).thenReturn(Optional.of(testCustomer));
        when(customerMapper.customerToCustomerDto(testCustomer)).thenReturn(testCustomerDto);

        // When
        Optional<CustomerDto> result = customerService.getCustomerById(1);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(1);
        assertThat(result.get().name()).isEqualTo("Test Customer");
        verify(customerRepository).findById(1);
        verify(customerMapper).customerToCustomerDto(testCustomer);
    }

    @Test
    void getCustomerByIdNotFound() {
        // Given
        when(customerRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<CustomerDto> result = customerService.getCustomerById(999);

        // Then
        assertThat(result).isEmpty();
        verify(customerRepository).findById(999);
    }

    @Test
    void getAllCustomers() {
        // Given
        when(customerRepository.findAll()).thenReturn(testCustomerList);
        when(customerMapper.customerToCustomerDto(testCustomer)).thenReturn(testCustomerDto);
        when(customerMapper.customerToCustomerDto(testCustomerList.get(1))).thenReturn(testCustomerDtoList.get(1));

        // When
        List<CustomerDto> result = customerService.getAllCustomers();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(1);
        assertThat(result.get(0).name()).isEqualTo("Test Customer");
        assertThat(result.get(1).id()).isEqualTo(2);
        assertThat(result.get(1).name()).isEqualTo("Another Customer");
        verify(customerRepository).findAll();
        verify(customerMapper).customerToCustomerDto(testCustomer);
        verify(customerMapper).customerToCustomerDto(testCustomerList.get(1));
    }

    @Test
    void updateCustomer() {
        // Given
        CustomerDto customerDtoToUpdate = new CustomerDto(
                null,
                null,
                "Updated Customer",
                "updated@example.com",
                "111-222-3333",
                null,
                null
        );

        Customer customerToUpdate = Customer.builder()
                .name("Updated Customer")
                .email("updated@example.com")
                .phone("111-222-3333")
                .build();

        Customer existingCustomer = Customer.builder()
                .id(1)
                .name("Test Customer")
                .email("test@example.com")
                .phone("123-456-7890")
                .build();

        Customer updatedCustomer = Customer.builder()
                .id(1)
                .name("Updated Customer")
                .email("updated@example.com")
                .phone("111-222-3333")
                .build();

        CustomerDto updatedCustomerDto = new CustomerDto(
                1,
                null,
                "Updated Customer",
                "updated@example.com",
                "111-222-3333",
                null,
                null
        );

        when(customerMapper.customerDtoToCustomer(customerDtoToUpdate)).thenReturn(customerToUpdate);
        when(customerRepository.findById(1)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);
        when(customerMapper.customerToCustomerDto(updatedCustomer)).thenReturn(updatedCustomerDto);

        // When
        Optional<CustomerDto> result = customerService.updateCustomer(1, customerDtoToUpdate);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(1);
        assertThat(result.get().name()).isEqualTo("Updated Customer");
        assertThat(result.get().email()).isEqualTo("updated@example.com");
        verify(customerMapper).customerDtoToCustomer(customerDtoToUpdate);
        verify(customerRepository).findById(1);
        verify(customerRepository).save(any(Customer.class));
        verify(customerMapper).customerToCustomerDto(updatedCustomer);
    }

    @Test
    void updateCustomerNotFound() {
        // Given
        CustomerDto customerDtoToUpdate = new CustomerDto(
                null,
                null,
                "Updated Customer",
                "updated@example.com",
                "111-222-3333",
                null,
                null
        );

        when(customerRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<CustomerDto> result = customerService.updateCustomer(999, customerDtoToUpdate);

        // Then
        assertThat(result).isEmpty();
        verify(customerRepository).findById(999);
    }

    @Test
    void deleteCustomer() {
        // Given
        when(customerRepository.findById(1)).thenReturn(Optional.of(testCustomer));

        // When
        boolean result = customerService.deleteCustomer(1);

        // Then
        assertThat(result).isTrue();
        verify(customerRepository).findById(1);
        verify(customerRepository).delete(testCustomer);
    }

    @Test
    void deleteCustomerNotFound() {
        // Given
        when(customerRepository.findById(999)).thenReturn(Optional.empty());

        // When
        boolean result = customerService.deleteCustomer(999);

        // Then
        assertThat(result).isFalse();
        verify(customerRepository).findById(999);
    }
}