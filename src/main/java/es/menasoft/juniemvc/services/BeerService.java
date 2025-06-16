package es.menasoft.juniemvc.services;

import es.menasoft.juniemvc.models.BeerDto;

import java.util.List;
import java.util.Optional;

public interface BeerService {

    /**
     * Save a new beer
     * @param beerDto the beer DTO to save
     * @return the saved beer DTO
     */
    BeerDto saveBeer(BeerDto beerDto);

    /**
     * Get a beer by its ID
     * @param id the beer ID
     * @return an Optional containing the beer DTO if found, or empty if not found
     */
    Optional<BeerDto> getBeerById(Integer id);

    /**
     * Get all beers
     * @return a list of all beer DTOs
     */
    List<BeerDto> getAllBeers();

    /**
     * Update an existing beer
     * @param id the ID of the beer to update
     * @param beerDto the updated beer DTO data
     * @return an Optional containing the updated beer DTO if found, or empty if not found
     */
    Optional<BeerDto> updateBeer(Integer id, BeerDto beerDto);

    /**
     * Delete a beer by its ID
     * @param id the ID of the beer to delete
     * @return true if the beer was deleted, false if it was not found
     */
    boolean deleteBeer(Integer id);
}
