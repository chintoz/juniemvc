package es.menasoft.juniemvc;

import es.menasoft.juniemvc.entities.Beer;
import es.menasoft.juniemvc.repositories.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test to verify database operations.
 * This test ensures that:
 * 1. The database schema is properly created by Hibernate
 * 2. The application can save and retrieve beer data
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.flyway.enabled=false"
})
public class FlywayMigrationIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BeerRepository beerRepository;

    @BeforeEach
    void setUp() {
        // Insert some test beers
        beerRepository.save(Beer.builder()
                .beerName("Guinness Draught")
                .beerStyle("Stout")
                .upc("123456")
                .quantityOnHand(100)
                .price(new BigDecimal("10.99"))
                .build());

        beerRepository.save(Beer.builder()
                .beerName("Sierra Nevada Pale Ale")
                .beerStyle("Pale Ale")
                .upc("654321")
                .quantityOnHand(200)
                .price(new BigDecimal("9.99"))
                .build());
    }

    /**
     * Test to verify that the database schema is properly created.
     */
    @Test
    void testDatabaseSchema() {
        // Check if the beer table exists (case-insensitive)
        Integer beerTableCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE UPPER(TABLE_NAME) = UPPER('beer')", Integer.class);
        assertThat(beerTableCount).isEqualTo(1);

        // Check if the customer table exists (case-insensitive)
        Integer customerTableCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE UPPER(TABLE_NAME) = UPPER('customer')", Integer.class);
        assertThat(customerTableCount).isEqualTo(1);

        // Check if the beer_order table exists (case-insensitive)
        Integer beerOrderTableCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE UPPER(TABLE_NAME) = UPPER('beer_order')", Integer.class);
        assertThat(beerOrderTableCount).isEqualTo(1);

        // Check if the order_line table exists (case-insensitive)
        Integer orderLineTableCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE UPPER(TABLE_NAME) = UPPER('order_line')", Integer.class);
        assertThat(orderLineTableCount).isEqualTo(1);
    }

    /**
     * Test to verify that the application can save and retrieve beer data.
     */
    @Test
    void testBeerData() {
        // Retrieve all beers using the repository
        List<Beer> beers = beerRepository.findAll();

        // Verify that we have at least 2 beers
        assertThat(beers).isNotEmpty();
        assertThat(beers.size()).isGreaterThanOrEqualTo(2);

        // Verify that specific beers exist
        boolean hasGuinness = beers.stream()
                .anyMatch(beer -> "Guinness Draught".equals(beer.getBeerName()));
        assertThat(hasGuinness).isTrue();

        boolean hasSierraNevada = beers.stream()
                .anyMatch(beer -> "Sierra Nevada Pale Ale".equals(beer.getBeerName()));
        assertThat(hasSierraNevada).isTrue();

        // Verify that beer data is complete
        Beer guinness = beers.stream()
                .filter(beer -> "Guinness Draught".equals(beer.getBeerName()))
                .findFirst()
                .orElseThrow();

        assertThat(guinness.getBeerStyle()).isEqualTo("Stout");
        assertThat(guinness.getUpc()).isEqualTo("123456");
        assertThat(guinness.getQuantityOnHand()).isEqualTo(100);
        assertThat(guinness.getPrice()).isEqualTo(new BigDecimal("10.99"));
    }
}
