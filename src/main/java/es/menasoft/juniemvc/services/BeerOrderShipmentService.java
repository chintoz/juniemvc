package es.menasoft.juniemvc.services;

import es.menasoft.juniemvc.models.BeerOrderShipmentDto;
import es.menasoft.juniemvc.models.CreateBeerOrderShipmentCommand;

import java.util.List;
import java.util.Optional;

public interface BeerOrderShipmentService {

    /**
     * Create a new beer order shipment
     * @param command the command containing the shipment details
     * @return the created beer order shipment DTO
     */
    BeerOrderShipmentDto createShipment(CreateBeerOrderShipmentCommand command);

    /**
     * Get a beer order shipment by its ID
     * @param id the beer order shipment ID
     * @return an Optional containing the beer order shipment DTO if found, or empty if not found
     */
    Optional<BeerOrderShipmentDto> getShipmentById(Integer id);

    /**
     * Get all beer order shipments
     * @return a list of all beer order shipment DTOs
     */
    List<BeerOrderShipmentDto> getAllShipments();

    /**
     * Get all beer order shipments for a beer order
     * @param beerOrderId the beer order ID
     * @return a list of beer order shipment DTOs for the beer order
     */
    List<BeerOrderShipmentDto> getShipmentsByBeerOrderId(Integer beerOrderId);

    /**
     * Update an existing beer order shipment
     * @param id the ID of the beer order shipment to update
     * @param shipmentDto the updated beer order shipment DTO data
     * @return an Optional containing the updated beer order shipment DTO if found, or empty if not found
     */
    Optional<BeerOrderShipmentDto> updateShipment(Integer id, BeerOrderShipmentDto shipmentDto);

    /**
     * Delete a beer order shipment by its ID
     * @param id the ID of the beer order shipment to delete
     * @return true if the beer order shipment was deleted, false if it was not found
     */
    boolean deleteShipment(Integer id);
}