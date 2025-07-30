package es.menasoft.juniemvc.controllers;

import es.menasoft.juniemvc.exceptions.EntityNotFoundException;
import es.menasoft.juniemvc.models.CustomerDto;
import es.menasoft.juniemvc.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing customers.
 * Provides endpoints for creating, retrieving, updating, and deleting customers.
 */
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
class CustomerController {

    private final CustomerService customerService;

    /**
     * Creates a new customer.
     *
     * @param customerDto the customer data
     * @return the created customer with status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        CustomerDto savedCustomer = customerService.saveCustomer(customerDto);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    /**
     * Retrieves a customer by its ID.
     *
     * @param customerId the ID of the customer to retrieve
     * @return the customer with status 200 (OK)
     * @throws EntityNotFoundException if the customer is not found
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable("customerId") Integer customerId) {
        CustomerDto customer = customerService.getCustomerById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer", customerId));
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    /**
     * Retrieves all customers.
     *
     * @return a list of all customers with status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customers = customerService.getAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    /**
     * Updates a customer.
     *
     * @param customerId the ID of the customer to update
     * @param customerDto the updated customer data
     * @return the updated customer with status 200 (OK)
     * @throws EntityNotFoundException if the customer is not found
     */
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable("customerId") Integer customerId, 
                                                     @Valid @RequestBody CustomerDto customerDto) {
        CustomerDto updatedCustomer = customerService.updateCustomer(customerId, customerDto);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    /**
     * Deletes a customer.
     *
     * @param customerId the ID of the customer to delete
     * @return status 204 (No Content) if successful
     * @throws EntityNotFoundException if the customer is not found
     */
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("customerId") Integer customerId) {
        if (!customerService.deleteCustomer(customerId)) {
            throw new EntityNotFoundException("Customer", customerId);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
