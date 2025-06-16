package es.menasoft.juniemvc.controllers;

import es.menasoft.juniemvc.exceptions.EntityNotFoundException;
import es.menasoft.juniemvc.models.BeerDto;
import es.menasoft.juniemvc.services.BeerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing beers.
 * Provides endpoints for creating, retrieving, updating, and deleting beers.
 */
@RestController
@RequestMapping("/api/v1/beers")
@RequiredArgsConstructor
class BeerController {

    private final BeerService beerService;

    /**
     * Creates a new beer.
     *
     * @param beerDto the beer data
     * @return the created beer with status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<BeerDto> createBeer(@Valid @RequestBody BeerDto beerDto) {
        BeerDto savedBeer = beerService.saveBeer(beerDto);
        return new ResponseEntity<>(savedBeer, HttpStatus.CREATED);
    }

    /**
     * Retrieves a beer by its ID.
     *
     * @param beerId the ID of the beer to retrieve
     * @return the beer with status 200 (OK)
     * @throws EntityNotFoundException if the beer is not found
     */
    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") Integer beerId) {
        BeerDto beer = beerService.getBeerById(beerId)
                .orElseThrow(() -> new EntityNotFoundException("Beer", beerId));
        return new ResponseEntity<>(beer, HttpStatus.OK);
    }

    /**
     * Retrieves all beers.
     *
     * @return a list of all beers with status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<BeerDto>> getAllBeers() {
        List<BeerDto> beers = beerService.getAllBeers();
        return new ResponseEntity<>(beers, HttpStatus.OK);
    }

    /**
     * Updates a beer.
     *
     * @param beerId the ID of the beer to update
     * @param beerDto the updated beer data
     * @return the updated beer with status 200 (OK)
     * @throws EntityNotFoundException if the beer is not found
     */
    @PutMapping("/{beerId}")
    public ResponseEntity<BeerDto> updateBeer(@PathVariable("beerId") Integer beerId, @Valid @RequestBody BeerDto beerDto) {
        BeerDto updatedBeer = beerService.updateBeer(beerId, beerDto)
                .orElseThrow(() -> new EntityNotFoundException("Beer", beerId));
        return new ResponseEntity<>(updatedBeer, HttpStatus.OK);
    }

    /**
     * Deletes a beer.
     *
     * @param beerId the ID of the beer to delete
     * @return status 204 (No Content) if successful
     * @throws EntityNotFoundException if the beer is not found
     */
    @DeleteMapping("/{beerId}")
    public ResponseEntity<Void> deleteBeer(@PathVariable("beerId") Integer beerId) {
        if (!beerService.deleteBeer(beerId)) {
            throw new EntityNotFoundException("Beer", beerId);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
