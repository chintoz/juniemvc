package es.menasoft.juniemvc.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BeerDto(
        // read-only
        Integer id,
        Integer version,

        @NotBlank(message = "Beer name is required")
        String beerName,

        // style of the beer, ALE, PALE ALE, IPA, etc.
        @NotBlank(message = "Beer style is required")
        String beerStyle,

        // Universal Product Code, a 13-digit number assigned to each unique beer product by the Federal Bar Assocation
        @NotBlank(message = "UPC is required")
        String upc,
        
        @NotNull(message = "Quantity on hand is required")
        @Positive(message = "Quantity on hand must be positive")
        Integer quantityOnHand,
        
        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        BigDecimal price,

        // read-only create data
        LocalDateTime createdDate,

        // read-only update date
        LocalDateTime updateDate
) {}