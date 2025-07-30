package es.menasoft.juniemvc.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateBeerOrderShipmentCommand(
    @NotNull(message = "Beer order ID is required")
    Integer beerOrderId,
    
    @NotNull(message = "Shipment date is required")
    LocalDate shipmentDate,
    
    @NotBlank(message = "Carrier is required")
    String carrier,
    
    @NotBlank(message = "Tracking number is required")
    String trackingNumber
) {}