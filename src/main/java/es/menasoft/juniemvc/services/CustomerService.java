package es.menasoft.juniemvc.services;

import es.menasoft.juniemvc.models.CustomerDto;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    /**
     * Save a new customer
     * @param customerDto the customer DTO to save
     * @return the saved customer DTO
     */
    CustomerDto saveCustomer(CustomerDto customerDto);

    /**
     * Get a customer by its ID
     * @param id the customer ID
     * @return an Optional containing the customer DTO if found, or empty if not found
     */
    Optional<CustomerDto> getCustomerById(Integer id);

    /**
     * Get all customers
     * @return a list of all customer DTOs
     */
    List<CustomerDto> getAllCustomers();

    /**
     * Update an existing customer
     * @param id the ID of the customer to update
     * @param customerDto the updated customer DTO data
     * @return an Optional containing the updated customer DTO if found, or empty if not found
     */
    Optional<CustomerDto> updateCustomer(Integer id, CustomerDto customerDto);

    /**
     * Delete a customer by its ID
     * @param id the ID of the customer to delete
     * @return true if the customer was deleted, false if it was not found
     */
    boolean deleteCustomer(Integer id);
}