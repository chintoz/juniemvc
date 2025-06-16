package es.menasoft.juniemvc.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BeerDto(
        Integer id,
        Integer version,
        
        @NotBlank(message = "Beer name is required")
        String beerName,
        
        @NotBlank(message = "Beer style is required")
        String beerStyle,
        
        @NotBlank(message = "UPC is required")
        String upc,
        
        @NotNull(message = "Quantity on hand is required")
        @Positive(message = "Quantity on hand must be positive")
        Integer quantityOnHand,
        
        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        BigDecimal price,
        
        LocalDateTime createdDate,
        LocalDateTime updateDate
) {}