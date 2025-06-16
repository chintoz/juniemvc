package es.menasoft.juniemvc.controllers;

import es.menasoft.juniemvc.models.BeerDto;
import es.menasoft.juniemvc.services.BeerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/beers")
@RequiredArgsConstructor
public class BeerController {

    private final BeerService beerService;

    @PostMapping
    public ResponseEntity<BeerDto> createBeer(@Valid @RequestBody BeerDto beerDto) {
        BeerDto savedBeer = beerService.saveBeer(beerDto);
        return new ResponseEntity<>(savedBeer, HttpStatus.CREATED);
    }

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") Integer beerId) {
        return beerService.getBeerById(beerId)
                .map(beer -> new ResponseEntity<>(beer, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<BeerDto>> getAllBeers() {
        List<BeerDto> beers = beerService.getAllBeers();
        return new ResponseEntity<>(beers, HttpStatus.OK);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<BeerDto> updateBeer(@PathVariable("beerId") Integer beerId, @Valid @RequestBody BeerDto beerDto) {
        return beerService.updateBeer(beerId, beerDto)
                .map(updatedBeer -> new ResponseEntity<>(updatedBeer, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{beerId}")
    public ResponseEntity<Void> deleteBeer(@PathVariable("beerId") Integer beerId) {
        boolean deleted = beerService.deleteBeer(beerId);
        return deleted ? 
                new ResponseEntity<>(HttpStatus.NO_CONTENT) : 
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
