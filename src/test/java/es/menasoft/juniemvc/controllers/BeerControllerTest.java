package es.menasoft.juniemvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.menasoft.juniemvc.models.BeerDto;
import es.menasoft.juniemvc.models.BeerPatchDto;
import es.menasoft.juniemvc.services.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.data.domain.Sort;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BeerControllerTest {

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

    private BeerDto testBeer;
    private List<BeerDto> testBeerList;

    @BeforeEach
    void setUp() {
        testBeer = new BeerDto(
                1,
                null,
                "Test Beer",
                "IPA",
                "description of test beer",
                "123456789",
                100,
                new BigDecimal("12.99"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        BeerDto testBeer2 = new BeerDto(
                2,
                null,
                "Another Beer",
                "Lager",
                "description of another beer",
                "987654321",
                200,
                new BigDecimal("9.99"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        testBeerList = Arrays.asList(testBeer, testBeer2);
    }

    @Test
    public void testCreateBeer() throws Exception {
        // Given
        BeerDto beerToSave = new BeerDto(
                null,
                null,
                "New Beer",
                "Stout",
                "description of new beer",
                "111222333",
                50,
                new BigDecimal("14.99"),
                null,
                null
        );

        given(beerService.saveBeer(any(BeerDto.class))).willReturn(
                new BeerDto(
                        3,
                        null,
                        "New Beer",
                        "Stout",
                        "description of new beer",
                        "111222333",
                        50,
                        new BigDecimal("14.99"),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );
    }

    @Test
    public void testCreateBeerValidationFail() throws Exception {
        // Given
        BeerDto invalidBeer = new BeerDto(
                null,
                null,
                "", // Invalid: empty name
                "Stout",
                "description of invalid beer",
                "111222333",
                -5, // Invalid: negative quantity
                new BigDecimal("-1.99"), // Invalid: negative price
                null,
                null
        );

        // When/Then
        mockMvc.perform(post("/api/v1/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidBeer)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    Exception exception = result.getResolvedException();
                    assert exception instanceof MethodArgumentNotValidException;
                });
    }

    @Test
    public void testGetBeerById() throws Exception {
        // Given
        given(beerService.getBeerById(1)).willReturn(Optional.of(testBeer));

        // When/Then
        mockMvc.perform(get("/api/v1/beers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.beerName", is("Test Beer")))
                .andExpect(jsonPath("$.beerStyle", is("IPA")));
    }

    @Test
    public void testGetBeerByIdNotFound() throws Exception {
        // Given
        given(beerService.getBeerById(999)).willReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/v1/beers/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllBeers() throws Exception {
        // Given
        given(beerService.getAllBeers()).willReturn(testBeerList);

        // When/Then
        mockMvc.perform(get("/api/v1/beers/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].beerName", is("Test Beer")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].beerName", is("Another Beer")));
    }
    
    @Test
    public void testGetBeersWithNameFilter() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 20);
        List<BeerDto> filteredList = Arrays.asList(testBeer);
        Page<BeerDto> beerPage = new PageImpl<>(filteredList, pageable, filteredList.size());
        
        given(beerService.getBeers(eq("Test"), eq(null), any(Pageable.class))).willReturn(beerPage);
        
        // When/Then
        mockMvc.perform(get("/api/v1/beers")
                .param("beerName", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].beerName", is("Test Beer")))
                .andExpect(jsonPath("$.totalElements", is(1)));
    }
    
    @Test
    public void testGetBeersWithPagination() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 1);
        List<BeerDto> pagedList = Arrays.asList(testBeer);
        Page<BeerDto> beerPage = new PageImpl<>(pagedList, pageable, testBeerList.size());
        
        given(beerService.getBeers(eq(null), eq(null), any(Pageable.class))).willReturn(beerPage);
        
        // When/Then
        mockMvc.perform(get("/api/v1/beers")
                .param("page", "0")
                .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].beerName", is("Test Beer")))
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.totalPages", is(2)));
    }
    
    @Test
    public void testGetBeersWithSorting() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "beerName"));
        Page<BeerDto> beerPage = new PageImpl<>(testBeerList, pageable, testBeerList.size());
        
        given(beerService.getBeers(eq(null), eq(null), any(Pageable.class))).willReturn(beerPage);
        
        // When/Then
        mockMvc.perform(get("/api/v1/beers")
                .param("sortField", "beerName")
                .param("sortDirection", "DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[1].id", is(2)));
    }
    
    @Test
    public void testGetBeersWithStyleFilter() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 20);
        List<BeerDto> filteredList = Arrays.asList(testBeer);
        Page<BeerDto> beerPage = new PageImpl<>(filteredList, pageable, filteredList.size());
        
        given(beerService.getBeers(eq(null), eq("IPA"), any(Pageable.class))).willReturn(beerPage);
        
        // When/Then
        mockMvc.perform(get("/api/v1/beers")
                .param("beerStyle", "IPA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].beerStyle", is("IPA")))
                .andExpect(jsonPath("$.totalElements", is(1)));
    }
    
    @Test
    public void testGetBeersWithNameAndStyleFilter() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 20);
        List<BeerDto> filteredList = Arrays.asList(testBeer);
        Page<BeerDto> beerPage = new PageImpl<>(filteredList, pageable, filteredList.size());
        
        given(beerService.getBeers(eq("Test"), eq("IPA"), any(Pageable.class))).willReturn(beerPage);
        
        // When/Then
        mockMvc.perform(get("/api/v1/beers")
                .param("beerName", "Test")
                .param("beerStyle", "IPA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].beerName", is("Test Beer")))
                .andExpect(jsonPath("$.content[0].beerStyle", is("IPA")))
                .andExpect(jsonPath("$.totalElements", is(1)));
    }

    @Test
    public void testUpdateBeer() throws Exception {
        // Given
        BeerDto beerToUpdate = new BeerDto(
                null,
                null,
                "Updated Beer",
                "Porter",
                "description of updated beer",
                "999888777",
                75,
                new BigDecimal("16.99"),
                null,
                null
        );

        BeerDto updatedBeer = new BeerDto(
                1,
                null,
                "Updated Beer",
                "Porter",
                "description of updated beer",
                "999888777",
                75,
                new BigDecimal("16.99"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        given(beerService.updateBeer(eq(1), any(BeerDto.class))).willReturn(Optional.of(updatedBeer));

        // When/Then
        mockMvc.perform(put("/api/v1/beers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.beerName", is("Updated Beer")))
                .andExpect(jsonPath("$.beerStyle", is("Porter")));
    }

    @Test
    public void testUpdateBeerNotFound() throws Exception {
        // Given
        BeerDto beerToUpdate = new BeerDto(
                null,
                null,
                "Updated Beer",
                "Porter",
                "description of updated beer",
                "999888777",
                75,
                new BigDecimal("16.99"),
                null,
                null
        );

        given(beerService.updateBeer(eq(999), any(BeerDto.class))).willReturn(Optional.empty());

        // When/Then
        mockMvc.perform(put("/api/v1/beers/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerToUpdate)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteBeer() throws Exception {
        // Given
        given(beerService.deleteBeer(1)).willReturn(true);

        // When/Then
        mockMvc.perform(delete("/api/v1/beers/1"))
                .andExpect(status().isNoContent());

        verify(beerService).deleteBeer(1);
    }

    @Test
    public void testDeleteBeerNotFound() throws Exception {
        // Given
        given(beerService.deleteBeer(999)).willReturn(false);

        // When/Then
        mockMvc.perform(delete("/api/v1/beers/999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testPatchBeer() throws Exception {
        // Given
        BeerPatchDto beerPatch = new BeerPatchDto(
                "Patched Beer",
                null,  // beerStyle not changed
                "Updated description via patch",
                null,  // upc not changed
                null,  // quantityOnHand not changed
                new BigDecimal("14.99")  // price changed
        );

        BeerDto patchedBeer = new BeerDto(
                1,
                null,
                "Patched Beer",
                "IPA",  // original style preserved
                "Updated description via patch",
                "123456",  // original upc preserved
                100,  // original quantity preserved
                new BigDecimal("14.99"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        given(beerService.patchBeer(eq(1), any(BeerPatchDto.class))).willReturn(Optional.of(patchedBeer));

        // When/Then
        mockMvc.perform(patch("/api/v1/beers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerPatch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.beerName", is("Patched Beer")))
                .andExpect(jsonPath("$.beerStyle", is("IPA")))  // unchanged
                .andExpect(jsonPath("$.description", is("Updated description via patch")))
                .andExpect(jsonPath("$.upc", is("123456")))  // unchanged
                .andExpect(jsonPath("$.quantityOnHand", is(100)))  // unchanged
                .andExpect(jsonPath("$.price", is(14.99)));  // changed
                
        verify(beerService).patchBeer(eq(1), any(BeerPatchDto.class));
    }
    
    @Test
    public void testPatchBeerNotFound() throws Exception {
        // Given
        BeerPatchDto beerPatch = new BeerPatchDto(
                "Patched Beer",
                null,
                null,
                null,
                null,
                null
        );

        given(beerService.patchBeer(eq(999), any(BeerPatchDto.class))).willReturn(Optional.empty());

        // When/Then
        mockMvc.perform(patch("/api/v1/beers/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerPatch)))
                .andExpect(status().isNotFound());
    }
}
