package es.menasoft.juniemvc.services;

import es.menasoft.juniemvc.entities.Beer;

import java.util.List;
import java.util.Optional;

public interface BeerService {

    /**
     * Save a new beer
     * @param beer the beer to save
     * @return the saved beer
     */
    Beer saveBeer(Beer beer);

    /**
     * Get a beer by its ID
     * @param id the beer ID
     * @return an Optional containing the beer if found, or empty if not found
     */
    Optional<Beer> getBeerById(Integer id);

    /**
     * Get all beers
     * @return a list of all beers
     */
    List<Beer> getAllBeers();

    /**
     * Update an existing beer
     * @param id the ID of the beer to update
     * @param beer the updated beer data
     * @return an Optional containing the updated beer if found, or empty if not found
     */
    Optional<Beer> updateBeer(Integer id, Beer beer);

    /**
     * Delete a beer by its ID
     * @param id the ID of the beer to delete
     * @return true if the beer was deleted, false if it was not found
     */
    boolean deleteBeer(Integer id);
}
