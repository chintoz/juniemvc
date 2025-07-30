package es.menasoft.juniemvc.controllers;

import es.menasoft.juniemvc.exceptions.EntityNotFoundException;
import es.menasoft.juniemvc.models.BeerOrderShipmentDto;
import es.menasoft.juniemvc.models.CreateBeerOrderShipmentCommand;
import es.menasoft.juniemvc.services.BeerOrderShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing beer order shipments.
 * Provides endpoints for creating, retrieving, updating, and deleting beer order shipments.
 */
@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
class BeerOrderShipmentController {

    private final BeerOrderShipmentService beerOrderShipmentService;

    /**
     * Creates a new beer order shipment.
     *
     * @param command the command containing the shipment details
     * @return the created beer order shipment with status 201 (Created)
     * @throws EntityNotFoundException if the beer order is not found
     */
    @PostMapping
    public ResponseEntity<BeerOrderShipmentDto> createShipment(@Valid @RequestBody CreateBeerOrderShipmentCommand command) {
        BeerOrderShipmentDto savedShipment = beerOrderShipmentService.createShipment(command);
        return new ResponseEntity<>(savedShipment, HttpStatus.CREATED);
    }

    /**
     * Retrieves a beer order shipment by its ID.
     *
     * @param shipmentId the ID of the beer order shipment to retrieve
     * @return the beer order shipment with status 200 (OK)
     * @throws EntityNotFoundException if the beer order shipment is not found
     */
    @GetMapping("/{shipmentId}")
    public ResponseEntity<BeerOrderShipmentDto> getShipmentById(@PathVariable("shipmentId") Integer shipmentId) {
        BeerOrderShipmentDto shipment = beerOrderShipmentService.getShipmentById(shipmentId)
                .orElseThrow(() -> new EntityNotFoundException("BeerOrderShipment", shipmentId));
        return new ResponseEntity<>(shipment, HttpStatus.OK);
    }

    /**
     * Retrieves all beer order shipments.
     *
     * @return a list of all beer order shipments with status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<BeerOrderShipmentDto>> getAllShipments() {
        List<BeerOrderShipmentDto> shipments = beerOrderShipmentService.getAllShipments();
        return new ResponseEntity<>(shipments, HttpStatus.OK);
    }

    /**
     * Updates a beer order shipment.
     *
     * @param shipmentId the ID of the beer order shipment to update
     * @param shipmentDto the updated beer order shipment data
     * @return the updated beer order shipment with status 200 (OK)
     * @throws EntityNotFoundException if the beer order shipment is not found
     */
    @PutMapping("/{shipmentId}")
    public ResponseEntity<BeerOrderShipmentDto> updateShipment(@PathVariable("shipmentId") Integer shipmentId, 
                                                              @Valid @RequestBody BeerOrderShipmentDto shipmentDto) {
        BeerOrderShipmentDto updatedShipment = beerOrderShipmentService.updateShipment(shipmentId, shipmentDto)
                .orElseThrow(() -> new EntityNotFoundException("BeerOrderShipment", shipmentId));
        return new ResponseEntity<>(updatedShipment, HttpStatus.OK);
    }

    /**
     * Deletes a beer order shipment.
     *
     * @param shipmentId the ID of the beer order shipment to delete
     * @return status 204 (No Content) if successful
     * @throws EntityNotFoundException if the beer order shipment is not found
     */
    @DeleteMapping("/{shipmentId}")
    public ResponseEntity<Void> deleteShipment(@PathVariable("shipmentId") Integer shipmentId) {
        if (!beerOrderShipmentService.deleteShipment(shipmentId)) {
            throw new EntityNotFoundException("BeerOrderShipment", shipmentId);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}