package es.menasoft.juniemvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.menasoft.juniemvc.exceptions.EntityNotFoundException;
import es.menasoft.juniemvc.models.CustomerDto;
import es.menasoft.juniemvc.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.flyway.enabled=false"
})
public class CustomerControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        CustomerService customerService() {
            return Mockito.mock(CustomerService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerService customerService;

    private CustomerDto testCustomer;
    private List<CustomerDto> testCustomerList;

    @BeforeEach
    void setUp() {
        testCustomer = new CustomerDto(
                1,
                null,
                "Test Customer",
                "test@example.com",
                "123-456-7890",
                "123 Main St",
                "Apt 4B",
                "Anytown",
                "CA",
                "12345",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        CustomerDto testCustomer2 = new CustomerDto(
                2,
                null,
                "Another Customer",
                "another@example.com",
                "987-654-3210",
                "456 Oak St",
                "Suite 7C",
                "Othertown",
                "NY",
                "67890",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        testCustomerList = Arrays.asList(testCustomer, testCustomer2);
    }

    @Test
    void testCreateCustomer() throws Exception {
        // Given
        CustomerDto customerToSave = new CustomerDto(
                null,
                null,
                "New Customer",
                "new@example.com",
                "555-555-5555",
                "789 New St",
                "Suite 3C",
                "Newtown",
                "CA",
                "54321",
                null,
                null
        );

        CustomerDto savedCustomer = new CustomerDto(
                3,
                null,
                "New Customer",
                "new@example.com",
                "555-555-5555",
                "789 New St",
                "Suite 3C",
                "Newtown",
                "CA",
                "54321",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        given(customerService.saveCustomer(any(CustomerDto.class))).willReturn(savedCustomer);

        // When/Then
        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerToSave)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("New Customer")))
                .andExpect(jsonPath("$.email", is("new@example.com")));
    }

    @Test
    void testCreateCustomerValidationFail() throws Exception {
        // Given
        CustomerDto invalidCustomer = new CustomerDto(
                null,
                null,
                "", // Invalid: empty name
                "invalid-email", // Invalid: not a valid email
                "123", // Invalid: too short
                "", // Invalid: empty address line 1
                null,
                "", // Invalid: empty city
                "", // Invalid: empty state
                "", // Invalid: empty postal code
                null,
                null
        );

        // When/Then
        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidCustomer)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    assert exception instanceof MethodArgumentNotValidException;
                });
    }

    @Test
    void testGetCustomerById() throws Exception {
        // Given
        given(customerService.getCustomerById(1)).willReturn(Optional.of(testCustomer));

        // When/Then
        mockMvc.perform(get("/api/v1/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Customer")))
                .andExpect(jsonPath("$.email", is("test@example.com")));
    }

    @Test
    void testGetCustomerByIdNotFound() throws Exception {
        // Given
        given(customerService.getCustomerById(999)).willReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/v1/customers/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllCustomers() throws Exception {
        // Given
        given(customerService.getAllCustomers()).willReturn(testCustomerList);

        // When/Then
        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test Customer")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Another Customer")));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        // Given
        CustomerDto customerToUpdate = new CustomerDto(
                null,
                null,
                "Updated Customer",
                "updated@example.com",
                "111-222-3333",
                "123 Updated St",
                "Apt 5C",
                "Newtown",
                "CA",
                "12345",
                null,
                null
        );

        CustomerDto updatedCustomer = new CustomerDto(
                1,
                null,
                "Updated Customer",
                "updated@example.com",
                "111-222-3333",
                "123 Updated St",
                "Apt 5C",
                "Newtown",
                "CA",
                "12345",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        given(customerService.updateCustomer(eq(1), any(CustomerDto.class))).willReturn(updatedCustomer);

        // When/Then
        mockMvc.perform(put("/api/v1/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Updated Customer")))
                .andExpect(jsonPath("$.email", is("updated@example.com")));
    }

    @Test
    void testUpdateCustomerNotFound() throws Exception {
        // Given
        CustomerDto customerToUpdate = new CustomerDto(
                null,
                null,
                "Updated Customer",
                "updated@example.com",
                "111-222-3333",
                "123 Updated St",
                "Apt 5C",
                "Newtown",
                "CA",
                "12345",
                null,
                null
        );

        given(customerService.updateCustomer(eq(999), any(CustomerDto.class)))
                .willThrow(new EntityNotFoundException("Customer", 999));

        // When/Then
        mockMvc.perform(put("/api/v1/customers/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerToUpdate)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteCustomer() throws Exception {
        // Given
        given(customerService.deleteCustomer(1)).willReturn(true);

        // When/Then
        mockMvc.perform(delete("/api/v1/customers/1"))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomer(1);
    }

    @Test
    void testDeleteCustomerNotFound() throws Exception {
        // Given
        given(customerService.deleteCustomer(999)).willReturn(false);

        // When/Then
        mockMvc.perform(delete("/api/v1/customers/999"))
                .andExpect(status().isNotFound());
    }
}