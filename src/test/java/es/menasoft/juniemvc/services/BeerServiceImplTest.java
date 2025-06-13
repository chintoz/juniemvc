package es.menasoft.juniemvc.services;

import es.menasoft.juniemvc.entities.Beer;
import es.menasoft.juniemvc.repositories.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BeerServiceImplTest {

    @Mock
    private BeerRepository beerRepository;

    @InjectMocks
    private BeerServiceImpl beerService;

    private Beer testBeer;
    private List<Beer> testBeerList;

    @BeforeEach
    void setUp() {
        testBeer = Beer.builder()
                .id(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456789")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer testBeer2 = Beer.builder()
                .id(2)
                .beerName("Another Beer")
                .beerStyle("Lager")
                .upc("987654321")
                .price(new BigDecimal("9.99"))
                .quantityOnHand(200)
                .build();

        testBeerList = Arrays.asList(testBeer, testBeer2);
    }

    @Test
    void saveBeer() {
        // Given
        Beer beerToSave = Beer.builder()
                .beerName("New Beer")
                .beerStyle("Stout")
                .upc("111222333")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(50)
                .build();

        Beer savedBeer = Beer.builder()
                .id(3)
                .beerName("New Beer")
                .beerStyle("Stout")
                .upc("111222333")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(50)
                .build();

        when(beerRepository.save(any(Beer.class))).thenReturn(savedBeer);

        // When
        Beer result = beerService.saveBeer(beerToSave);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3);
        assertThat(result.getBeerName()).isEqualTo("New Beer");
        verify(beerRepository).save(beerToSave);
    }

    @Test
    void getBeerById() {
        // Given
        when(beerRepository.findById(1)).thenReturn(Optional.of(testBeer));

        // When
        Optional<Beer> result = beerService.getBeerById(1);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
        assertThat(result.get().getBeerName()).isEqualTo("Test Beer");
        verify(beerRepository).findById(1);
    }

    @Test
    void getBeerByIdNotFound() {
        // Given
        when(beerRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<Beer> result = beerService.getBeerById(999);

        // Then
        assertThat(result).isEmpty();
        verify(beerRepository).findById(999);
    }

    @Test
    void getAllBeers() {
        // Given
        when(beerRepository.findAll()).thenReturn(testBeerList);

        // When
        List<Beer> result = beerService.getAllBeers();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1);
        assertThat(result.get(0).getBeerName()).isEqualTo("Test Beer");
        assertThat(result.get(1).getId()).isEqualTo(2);
        assertThat(result.get(1).getBeerName()).isEqualTo("Another Beer");
        verify(beerRepository).findAll();
    }

    @Test
    void updateBeer() {
        // Given
        Beer beerToUpdate = Beer.builder()
                .beerName("Updated Beer")
                .beerStyle("Porter")
                .upc("999888777")
                .price(new BigDecimal("16.99"))
                .quantityOnHand(75)
                .build();

        Beer existingBeer = Beer.builder()
                .id(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456789")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer updatedBeer = Beer.builder()
                .id(1)
                .beerName("Updated Beer")
                .beerStyle("Porter")
                .upc("999888777")
                .price(new BigDecimal("16.99"))
                .quantityOnHand(75)
                .build();

        when(beerRepository.findById(1)).thenReturn(Optional.of(existingBeer));
        when(beerRepository.save(any(Beer.class))).thenReturn(updatedBeer);

        // When
        Optional<Beer> result = beerService.updateBeer(1, beerToUpdate);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
        assertThat(result.get().getBeerName()).isEqualTo("Updated Beer");
        assertThat(result.get().getBeerStyle()).isEqualTo("Porter");
        verify(beerRepository).findById(1);
        verify(beerRepository).save(any(Beer.class));
    }

    @Test
    void updateBeerNotFound() {
        // Given
        Beer beerToUpdate = Beer.builder()
                .beerName("Updated Beer")
                .beerStyle("Porter")
                .upc("999888777")
                .price(new BigDecimal("16.99"))
                .quantityOnHand(75)
                .build();

        when(beerRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<Beer> result = beerService.updateBeer(999, beerToUpdate);

        // Then
        assertThat(result).isEmpty();
        verify(beerRepository).findById(999);
    }

    @Test
    void deleteBeer() {
        // Given
        when(beerRepository.findById(1)).thenReturn(Optional.of(testBeer));

        // When
        boolean result = beerService.deleteBeer(1);

        // Then
        assertThat(result).isTrue();
        verify(beerRepository).findById(1);
        verify(beerRepository).delete(testBeer);
    }

    @Test
    void deleteBeerNotFound() {
        // Given
        when(beerRepository.findById(999)).thenReturn(Optional.empty());

        // When
        boolean result = beerService.deleteBeer(999);

        // Then
        assertThat(result).isFalse();
        verify(beerRepository).findById(999);
    }
}