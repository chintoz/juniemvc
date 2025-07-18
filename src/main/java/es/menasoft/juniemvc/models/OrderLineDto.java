package es.menasoft.juniemvc.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderLineDto(
        Integer id,
        
        @NotNull(message = "Order quantity is required")
        @Positive(message = "Order quantity must be positive")
        Integer orderQuantity,

        // Reference to an existing Beer in the catalog.
        @NotNull(message = "Beer ID is required")
        Integer beerId,

        //  Name of the beer
        String beerName
) {}