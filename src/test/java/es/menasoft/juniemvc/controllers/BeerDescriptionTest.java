package es.menasoft.juniemvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.menasoft.juniemvc.models.BeerDto;
import es.menasoft.juniemvc.services.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class specifically for testing the beer description property
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BeerDescriptionTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        BeerService beerService() {
            return Mockito.mock(BeerService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BeerService beerService;

    @Test
    public void testBeerDescription() throws Exception {
        // Given
        String description = "A rich, dark stout with notes of chocolate and coffee.";
        BeerDto beerWithDescription = new BeerDto(
                null,
                null,
                "Chocolate Stout",
                "Stout",
                description,
                "555666777",
                80,
                new BigDecimal("11.99"),
                null,
                null
        );
        
        BeerDto savedBeer = new BeerDto(
                4,
                null,
                "Chocolate Stout",
                "Stout",
                description,
                "555666777",
                80,
                new BigDecimal("11.99"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        
        given(beerService.saveBeer(any(BeerDto.class))).willReturn(savedBeer);
        given(beerService.getBeerById(4)).willReturn(Optional.of(savedBeer));
        
        // When/Then - Create beer with description
        mockMvc.perform(post("/api/v1/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerWithDescription)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description", is(description)));
                
        // When/Then - Get beer and verify description
        mockMvc.perform(get("/api/v1/beers/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.beerName", is("Chocolate Stout")))
                .andExpect(jsonPath("$.description", is(description)));
    }
    
    @Test
    public void testBeerDescriptionMapping() throws Exception {
        // Given
        String description = "A hoppy IPA with citrus notes and a bitter finish.";
        BeerDto beerDto = new BeerDto(
                1,
                null,
                "Test IPA",
                "IPA",
                description,
                "123456789",
                100,
                new BigDecimal("12.99"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        
        given(beerService.getBeerById(1)).willReturn(Optional.of(beerDto));
        
        // When/Then - Get beer and verify description
        mockMvc.perform(get("/api/v1/beers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(description)));
    }
}