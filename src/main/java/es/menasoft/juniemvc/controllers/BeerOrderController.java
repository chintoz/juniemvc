package es.menasoft.juniemvc.controllers;

import es.menasoft.juniemvc.exceptions.EntityNotFoundException;
import es.menasoft.juniemvc.models.BeerOrderDto;
import es.menasoft.juniemvc.models.CreateBeerOrderCommand;
import es.menasoft.juniemvc.services.BeerOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing beer orders.
 * Provides endpoints for creating, retrieving, updating, and deleting beer orders.
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
class BeerOrderController {

    private final BeerOrderService beerOrderService;

    /**
     * Creates a new beer order.
     *
     * @param command the command containing the order details
     * @return the created beer order with status 201 (Created)
     * @throws EntityNotFoundException if the customer or beer is not found
     */
    @PostMapping
    public ResponseEntity<BeerOrderDto> createBeerOrder(@Valid @RequestBody CreateBeerOrderCommand command) {
        BeerOrderDto savedOrder = beerOrderService.createBeerOrder(command);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    /**
     * Retrieves a beer order by its ID.
     *
     * @param orderId the ID of the beer order to retrieve
     * @return the beer order with status 200 (OK)
     * @throws EntityNotFoundException if the beer order is not found
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<BeerOrderDto> getBeerOrderById(@PathVariable("orderId") Integer orderId) {
        BeerOrderDto order = beerOrderService.getBeerOrderById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("BeerOrder", orderId));
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    /**
     * Retrieves all beer orders.
     *
     * @return a list of all beer orders with status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<BeerOrderDto>> getAllBeerOrders() {
        List<BeerOrderDto> orders = beerOrderService.getAllBeerOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    /**
     * Retrieves all beer orders for a specific customer.
     *
     * @param customerId the ID of the customer
     * @return a list of beer orders for the customer with status 200 (OK)
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BeerOrderDto>> getBeerOrdersByCustomerId(@PathVariable("customerId") Integer customerId) {
        List<BeerOrderDto> orders = beerOrderService.getBeerOrdersByCustomerId(customerId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    /**
     * Updates a beer order.
     *
     * @param orderId the ID of the beer order to update
     * @param beerOrderDto the updated beer order data
     * @return the updated beer order with status 200 (OK)
     * @throws EntityNotFoundException if the beer order is not found
     */
    @PutMapping("/{orderId}")
    public ResponseEntity<BeerOrderDto> updateBeerOrder(@PathVariable("orderId") Integer orderId, 
                                                       @Valid @RequestBody BeerOrderDto beerOrderDto) {
        BeerOrderDto updatedOrder = beerOrderService.updateBeerOrder(orderId, beerOrderDto)
                .orElseThrow(() -> new EntityNotFoundException("BeerOrder", orderId));
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    /**
     * Updates the status of a beer order.
     *
     * @param orderId the ID of the beer order to update
     * @param status the new status
     * @return the updated beer order with status 200 (OK)
     * @throws EntityNotFoundException if the beer order is not found
     */
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<BeerOrderDto> updateBeerOrderStatus(@PathVariable("orderId") Integer orderId, 
                                                             @RequestBody String status) {
        BeerOrderDto updatedOrder = beerOrderService.updateBeerOrderStatus(orderId, status)
                .orElseThrow(() -> new EntityNotFoundException("BeerOrder", orderId));
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    /**
     * Deletes a beer order.
     *
     * @param orderId the ID of the beer order to delete
     * @return status 204 (No Content) if successful
     * @throws EntityNotFoundException if the beer order is not found
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteBeerOrder(@PathVariable("orderId") Integer orderId) {
        if (!beerOrderService.deleteBeerOrder(orderId)) {
            throw new EntityNotFoundException("BeerOrder", orderId);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
