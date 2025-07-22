package es.menasoft.juniemvc.repositories;

import es.menasoft.juniemvc.entities.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    
    @Test
    void testFindAllWithPagination() {
        // Create test beers
        Beer beer1 = Beer.builder()
                .beerName("Pagination Test Beer 1")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer beer2 = Beer.builder()
                .beerName("Pagination Test Beer 2")
                .beerStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(200)
                .build();

        beerRepository.save(beer1);
        beerRepository.save(beer2);

        // Test pagination
        Pageable pageable = PageRequest.of(0, 10);
        Page<Beer> beerPage = beerRepository.findAll(pageable);

        assertThat(beerPage).isNotNull();
        assertThat(beerPage.getContent().size()).isGreaterThanOrEqualTo(2);
        assertThat(beerPage.getTotalElements()).isGreaterThanOrEqualTo(2);
    }
    
    @Test
    void testFindByBeerNameContainingIgnoreCase() {
        // Create test beers with different names
        Beer beer1 = Beer.builder()
                .beerName("Special IPA")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer beer2 = Beer.builder()
                .beerName("Regular Stout")
                .beerStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(200)
                .build();
                
        Beer beer3 = Beer.builder()
                .beerName("Special Lager")
                .beerStyle("Lager")
                .upc("789012")
                .price(new BigDecimal("10.99"))
                .quantityOnHand(150)
                .build();

        beerRepository.save(beer1);
        beerRepository.save(beer2);
        beerRepository.save(beer3);

        // Test finding by name containing "Special" (case insensitive)
        Pageable pageable = PageRequest.of(0, 10, Sort.by("beerName"));
        Page<Beer> specialBeers = beerRepository.findByBeerNameContainingIgnoreCase("special", pageable);

        assertThat(specialBeers).isNotNull();
        assertThat(specialBeers.getContent().size()).isEqualTo(2);
        assertThat(specialBeers.getContent().get(0).getBeerName()).contains("Special");
        assertThat(specialBeers.getContent().get(1).getBeerName()).contains("Special");
        
        // Test finding by name containing "IPA" (case insensitive)
        Page<Beer> ipaBeers = beerRepository.findByBeerNameContainingIgnoreCase("ipa", pageable);
        
        assertThat(ipaBeers).isNotNull();
        assertThat(ipaBeers.getContent().size()).isEqualTo(1);
        assertThat(ipaBeers.getContent().get(0).getBeerName()).isEqualTo("Special IPA");
        
        // Test with no matches
        Page<Beer> noMatches = beerRepository.findByBeerNameContainingIgnoreCase("nonexistent", pageable);
        
        assertThat(noMatches).isNotNull();
        assertThat(noMatches.getContent()).isEmpty();
    }
    
    @Test
    void testFindByBeerStyleContainingIgnoreCase() {
        // Create test beers with different styles
        Beer beer1 = Beer.builder()
                .beerName("Special IPA")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer beer2 = Beer.builder()
                .beerName("Double IPA")
                .beerStyle("Double IPA")
                .upc("654321")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(200)
                .build();
                
        Beer beer3 = Beer.builder()
                .beerName("Special Lager")
                .beerStyle("Lager")
                .upc("789012")
                .price(new BigDecimal("10.99"))
                .quantityOnHand(150)
                .build();

        beerRepository.save(beer1);
        beerRepository.save(beer2);
        beerRepository.save(beer3);

        // Test finding by style containing "IPA" (case insensitive)
        Pageable pageable = PageRequest.of(0, 10, Sort.by("beerName"));
        Page<Beer> ipaBeers = beerRepository.findByBeerStyleContainingIgnoreCase("ipa", pageable);

        assertThat(ipaBeers).isNotNull();
        assertThat(ipaBeers.getContent().size()).isEqualTo(2);
        assertThat(ipaBeers.getContent().get(0).getBeerStyle()).contains("IPA");
        assertThat(ipaBeers.getContent().get(1).getBeerStyle()).contains("IPA");
        
        // Test finding by style containing "Lager" (case insensitive)
        Page<Beer> lagerBeers = beerRepository.findByBeerStyleContainingIgnoreCase("lager", pageable);
        
        assertThat(lagerBeers).isNotNull();
        assertThat(lagerBeers.getContent().size()).isEqualTo(1);
        assertThat(lagerBeers.getContent().get(0).getBeerStyle()).isEqualTo("Lager");
        
        // Test with no matches
        Page<Beer> noMatches = beerRepository.findByBeerStyleContainingIgnoreCase("nonexistent", pageable);
        
        assertThat(noMatches).isNotNull();
        assertThat(noMatches.getContent()).isEmpty();
    }
    
    @Test
    void testFindByBeerNameAndBeerStyleContainingIgnoreCase() {
        // Create test beers with different names and styles
        Beer beer1 = Beer.builder()
                .beerName("Special IPA")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer beer2 = Beer.builder()
                .beerName("Special Stout")
                .beerStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(200)
                .build();
                
        Beer beer3 = Beer.builder()
                .beerName("Regular IPA")
                .beerStyle("IPA")
                .upc("789012")
                .price(new BigDecimal("10.99"))
                .quantityOnHand(150)
                .build();

        beerRepository.save(beer1);
        beerRepository.save(beer2);
        beerRepository.save(beer3);

        // Test finding by name and style (case insensitive)
        Pageable pageable = PageRequest.of(0, 10, Sort.by("beerName"));
        Page<Beer> specialIpaBeers = beerRepository.findByBeerNameContainingIgnoreCaseAndBeerStyleContainingIgnoreCase(
                "special", "ipa", pageable);

        assertThat(specialIpaBeers).isNotNull();
        assertThat(specialIpaBeers.getContent().size()).isEqualTo(1);
        assertThat(specialIpaBeers.getContent().get(0).getBeerName()).isEqualTo("Special IPA");
        assertThat(specialIpaBeers.getContent().get(0).getBeerStyle()).isEqualTo("IPA");
        
        // Test with no matches
        Page<Beer> noMatches = beerRepository.findByBeerNameContainingIgnoreCaseAndBeerStyleContainingIgnoreCase(
                "nonexistent", "nonexistent", pageable);
        
        assertThat(noMatches).isNotNull();
        assertThat(noMatches.getContent()).isEmpty();
    }
}
