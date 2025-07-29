package es.menasoft.juniemvc.models;

import java.math.BigDecimal;

/**
 * DTO for partial updates of Beer entities.
 * All fields are nullable to allow for partial updates.
 */
public record BeerPatchDto(
        String beerName,
        String beerStyle,
        String description,
        String upc,
        Integer quantityOnHand,
        BigDecimal price
) {}