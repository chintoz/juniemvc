package es.menasoft.juniemvc.services;

import es.menasoft.juniemvc.models.BeerOrderDto;
import es.menasoft.juniemvc.models.CreateBeerOrderCommand;

import java.util.List;
import java.util.Optional;

public interface BeerOrderService {

    /**
     * Create a new beer order
     * @param command the command containing the order details
     * @return the created beer order DTO
     */
    BeerOrderDto createBeerOrder(CreateBeerOrderCommand command);

    /**
     * Get a beer order by its ID
     * @param id the beer order ID
     * @return an Optional containing the beer order DTO if found, or empty if not found
     */
    Optional<BeerOrderDto> getBeerOrderById(Integer id);

    /**
     * Get all beer orders
     * @return a list of all beer order DTOs
     */
    List<BeerOrderDto> getAllBeerOrders();

    /**
     * Get all beer orders for a customer
     * @param customerId the customer ID
     * @return a list of beer order DTOs for the customer
     */
    List<BeerOrderDto> getBeerOrdersByCustomerId(Integer customerId);

    /**
     * Update an existing beer order
     * @param id the ID of the beer order to update
     * @param beerOrderDto the updated beer order DTO data
     * @return an Optional containing the updated beer order DTO if found, or empty if not found
     */
    Optional<BeerOrderDto> updateBeerOrder(Integer id, BeerOrderDto beerOrderDto);

    /**
     * Update the status of a beer order
     * @param id the ID of the beer order to update
     * @param status the new status
     * @return an Optional containing the updated beer order DTO if found, or empty if not found
     */
    Optional<BeerOrderDto> updateBeerOrderStatus(Integer id, String status);

    /**
     * Delete a beer order by its ID
     * @param id the ID of the beer order to delete
     * @return true if the beer order was deleted, false if it was not found
     */
    boolean deleteBeerOrder(Integer id);
}