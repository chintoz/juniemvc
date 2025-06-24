package es.menasoft.juniemvc.repositories;

import es.menasoft.juniemvc.entities.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for BeerRepository.
 * Uses DataJpaTest with custom properties to ensure tests run correctly.
 */
@DataJpaTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.flyway.enabled=false"
})
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        Beer beer = Beer.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer savedBeer = beerRepository.save(beer);

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

    @Test
    void testGetBeerById() {
        Beer beer = Beer.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer savedBeer = beerRepository.save(beer);

        Optional<Beer> fetchedBeerOptional = beerRepository.findById(savedBeer.getId());

        assertThat(fetchedBeerOptional).isPresent();
        Beer fetchedBeer = fetchedBeerOptional.get();
        assertThat(fetchedBeer.getBeerName()).isEqualTo("Test Beer");
    }

    @Test
    void testListBeers() {
        Beer beer1 = Beer.builder()
                .beerName("Beer 1")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer beer2 = Beer.builder()
                .beerName("Beer 2")
                .beerStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(200)
                .build();

        beerRepository.save(beer1);
        beerRepository.save(beer2);

        List<Beer> beers = beerRepository.findAll();

        assertThat(beers).isNotNull();
        assertThat(beers.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    @Rollback
    void testUpdateBeer() {
        Beer beer = Beer.builder()
                .beerName("Original Name")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer savedBeer = beerRepository.save(beer);

        savedBeer.setBeerName("Updated Name");
        Beer updatedBeer = beerRepository.save(savedBeer);

        assertThat(updatedBeer.getBeerName()).isEqualTo("Updated Name");
    }

    @Test
    @Rollback
    void testDeleteBeer() {
        Beer beer = Beer.builder()
                .beerName("Delete Me")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer savedBeer = beerRepository.save(beer);

        beerRepository.deleteById(savedBeer.getId());

        Optional<Beer> deletedBeer = beerRepository.findById(savedBeer.getId());

        assertThat(deletedBeer).isEmpty();
    }
}
