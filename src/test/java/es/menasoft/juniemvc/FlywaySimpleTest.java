package es.menasoft.juniemvc;

import es.menasoft.juniemvc.entities.Beer;
import es.menasoft.juniemvc.repositories.BeerRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Simple test to verify that the application context loads correctly.
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.flyway.enabled=false"
})
@Order(1)
public class FlywaySimpleTest {

    @Autowired
    private BeerRepository beerRepository;

    /**
     * Test to verify that the application context loads and the repository works.
     */
    @Test
    void testApplicationContextLoads() {
        // Insert a test beer
        Beer testBeer = Beer.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456789")
                .quantityOnHand(100)
                .price(new BigDecimal("10.99"))
                .build();

        beerRepository.save(testBeer);

        // Check if the beer was saved
        List<Beer> beers = beerRepository.findAll();
        assertThat(beers).isNotEmpty();
        Beer savedBeer = beers.stream().filter(beer -> beer.getUpc().equals("123456789")).findFirst().orElseThrow();
        assertThat(savedBeer.getBeerName()).isEqualTo("Test Beer");
    }
}
