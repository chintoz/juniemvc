package es.menasoft.juniemvc.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BeerOrderShipmentDto(
    // Read-only
    Integer id,
    Integer version,
    
    @NotNull(message = "Shipment date is required")
    LocalDate shipmentDate,
    
    @NotBlank(message = "Carrier is required")
    String carrier,
    
    @NotBlank(message = "Tracking number is required")
    String trackingNumber,
    
    // Read-only create date
    LocalDateTime createdDate,
    
    // Read-only update date
    LocalDateTime updateDate,
    
    @NotNull(message = "Beer order ID is required")
    Integer beerOrderId
) {}