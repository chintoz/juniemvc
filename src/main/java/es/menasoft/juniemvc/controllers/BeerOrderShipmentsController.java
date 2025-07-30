package es.menasoft.juniemvc.controllers;

import es.menasoft.juniemvc.models.BeerOrderShipmentDto;
import es.menasoft.juniemvc.services.BeerOrderShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing beer order shipments by beer order.
 * Provides endpoints for retrieving shipments for a specific beer order.
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
class BeerOrderShipmentsController {

    private final BeerOrderShipmentService beerOrderShipmentService;

    /**
     * Retrieves all shipments for a specific beer order.
     *
     * @param orderId the ID of the beer order
     * @return a list of beer order shipments for the beer order with status 200 (OK)
     */
    @GetMapping("/{orderId}/shipments")
    public ResponseEntity<List<BeerOrderShipmentDto>> getShipmentsByBeerOrderId(@PathVariable("orderId") Integer orderId) {
        List<BeerOrderShipmentDto> shipments = beerOrderShipmentService.getShipmentsByBeerOrderId(orderId);
        return new ResponseEntity<>(shipments, HttpStatus.OK);
    }
}