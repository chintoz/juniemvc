package es.menasoft.juniemvc.repositories;

import es.menasoft.juniemvc.entities.Beer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeerRepository extends JpaRepository<Beer, Integer> {
    // Spring Data JPA will automatically implement basic CRUD operations
    // We can add custom query methods here if needed
    
    /**
     * Find beers by name containing the given string (case-insensitive)
     * @param beerName the name to search for
     * @param pageable pagination information
     * @return a page of beers matching the search criteria
     */
    Page<Beer> findByBeerNameContainingIgnoreCase(String beerName, Pageable pageable);
    
    /**
     * Find all beers with pagination
     * @param pageable pagination information
     * @return a page of all beers
     */
    Page<Beer> findAll(Pageable pageable);
}