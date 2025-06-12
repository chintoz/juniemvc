package es.menasoft.juniemvc.controllers;

import es.menasoft.juniemvc.entities.Beer;
import es.menasoft.juniemvc.services.BeerService;
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
    public ResponseEntity<Beer> createBeer(@RequestBody Beer beer) {
        Beer savedBeer = beerService.saveBeer(beer);
        return new ResponseEntity<>(savedBeer, HttpStatus.CREATED);
    }

    @GetMapping("/{beerId}")
    public ResponseEntity<Beer> getBeerById(@PathVariable("beerId") Integer beerId) {
        return beerService.getBeerById(beerId)
                .map(beer -> new ResponseEntity<>(beer, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Beer>> getAllBeers() {
        List<Beer> beers = beerService.getAllBeers();
        return new ResponseEntity<>(beers, HttpStatus.OK);
    }
}
