package es.menasoft.juniemvc.services;

import es.menasoft.juniemvc.entities.Beer;
import es.menasoft.juniemvc.mappers.BeerMapper;
import es.menasoft.juniemvc.models.BeerDto;
import es.menasoft.juniemvc.models.BeerPatchDto;
import es.menasoft.juniemvc.repositories.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BeerServiceImplTest {

    @Mock
    private BeerRepository beerRepository;

    @Mock
    private BeerMapper beerMapper;

    @InjectMocks
    private BeerServiceImpl beerService;

    private Beer testBeer;
    private BeerDto testBeerDto;
    private List<Beer> testBeerList;
    private List<BeerDto> testBeerDtoList;

    @BeforeEach
    void setUp() {
        // Setup test Beer entity
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

        // Setup test BeerDto
        testBeerDto = new BeerDto(
                1,
                null,
                "Test Beer",
                "IPA",
                "description of Test Beer",
                "123456789",
                100,
                new BigDecimal("12.99"),
                null,
                null
        );

        BeerDto testBeerDto2 = new BeerDto(
                2,
                null,
                "Another Beer",
                "Lager",
                "description of Another Beer",
                "987654321",
                200,
                new BigDecimal("9.99"),
                null,
                null
        );

        testBeerDtoList = Arrays.asList(testBeerDto, testBeerDto2);
    }

    @Test
    void saveBeer() {
        // Given
        BeerDto beerDtoToSave = new BeerDto(
                null,
                null,
                "New Beer",
                "Stout",
                "description of New Beer",
                "111222333",
                50,
                new BigDecimal("14.99"),
                null,
                null
        );

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

        BeerDto savedBeerDto = new BeerDto(
                3,
                null,
                "New Beer",
                "Stout",
                "description of New Beer",
                "111222333",
                50,
                new BigDecimal("14.99"),
                null,
                null
        );

        when(beerMapper.beerDtoToBeer(beerDtoToSave)).thenReturn(beerToSave);
        when(beerRepository.save(any(Beer.class))).thenReturn(savedBeer);
        when(beerMapper.beerToBeerDto(savedBeer)).thenReturn(savedBeerDto);

        // When
        BeerDto result = beerService.saveBeer(beerDtoToSave);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(3);
        assertThat(result.beerName()).isEqualTo("New Beer");
        verify(beerMapper).beerDtoToBeer(beerDtoToSave);
        verify(beerRepository).save(any(Beer.class));
        verify(beerMapper).beerToBeerDto(savedBeer);
    }

    @Test
    void getBeerById() {
        // Given
        when(beerRepository.findById(1)).thenReturn(Optional.of(testBeer));
        when(beerMapper.beerToBeerDto(testBeer)).thenReturn(testBeerDto);

        // When
        Optional<BeerDto> result = beerService.getBeerById(1);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(1);
        assertThat(result.get().beerName()).isEqualTo("Test Beer");
        verify(beerRepository).findById(1);
        verify(beerMapper).beerToBeerDto(testBeer);
    }

    @Test
    void getBeerByIdNotFound() {
        // Given
        when(beerRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<BeerDto> result = beerService.getBeerById(999);

        // Then
        assertThat(result).isEmpty();
        verify(beerRepository).findById(999);
    }

    @Test
    void getAllBeers() {
        // Given
        when(beerRepository.findAll()).thenReturn(testBeerList);
        when(beerMapper.beerToBeerDto(testBeer)).thenReturn(testBeerDto);
        when(beerMapper.beerToBeerDto(testBeerList.get(1))).thenReturn(testBeerDtoList.get(1));

        // When
        List<BeerDto> result = beerService.getAllBeers();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(1);
        assertThat(result.get(0).beerName()).isEqualTo("Test Beer");
        assertThat(result.get(1).id()).isEqualTo(2);
        assertThat(result.get(1).beerName()).isEqualTo("Another Beer");
        verify(beerRepository).findAll();
        verify(beerMapper).beerToBeerDto(testBeer);
        verify(beerMapper).beerToBeerDto(testBeerList.get(1));
    }

    @Test
    void updateBeer() {
        // Given
        BeerDto beerDtoToUpdate = new BeerDto(
                null,
                null,
                "Updated Beer",
                "Porter",
                "description of Updated Beer",
                "999888777",
                75,
                new BigDecimal("16.99"),
                null,
                null
        );

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

        BeerDto updatedBeerDto = new BeerDto(
                1,
                null,
                "Updated Beer",
                "Porter",
                "description of Updated Beer",
                "999888777",
                75,
                new BigDecimal("16.99"),
                null,
                null
        );

        when(beerMapper.beerDtoToBeer(beerDtoToUpdate)).thenReturn(beerToUpdate);
        when(beerRepository.findById(1)).thenReturn(Optional.of(existingBeer));
        when(beerRepository.save(any(Beer.class))).thenReturn(updatedBeer);
        when(beerMapper.beerToBeerDto(updatedBeer)).thenReturn(updatedBeerDto);

        // When
        Optional<BeerDto> result = beerService.updateBeer(1, beerDtoToUpdate);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(1);
        assertThat(result.get().beerName()).isEqualTo("Updated Beer");
        assertThat(result.get().beerStyle()).isEqualTo("Porter");
        verify(beerMapper).beerDtoToBeer(beerDtoToUpdate);
        verify(beerRepository).findById(1);
        verify(beerRepository).save(any(Beer.class));
        verify(beerMapper).beerToBeerDto(updatedBeer);
    }

    @Test
    void updateBeerNotFound() {
        // Given
        BeerDto beerDtoToUpdate = new BeerDto(
                null,
                null,
                "Updated Beer",
                "Porter",
                "description of Updated Beer",
                "999888777",
                75,
                new BigDecimal("16.99"),
                null,
                null
        );

        when(beerRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<BeerDto> result = beerService.updateBeer(999, beerDtoToUpdate);

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
    
    @Test
    void getBeersWithNameFilter() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Beer> filteredBeerList = Arrays.asList(testBeer);
        Page<Beer> filteredBeerPage = new PageImpl<>(filteredBeerList, pageable, filteredBeerList.size());
        
        when(beerRepository.findByBeerNameContainingIgnoreCase(eq("Test"), any(Pageable.class)))
            .thenReturn(filteredBeerPage);
        when(beerMapper.beerToBeerDto(any(Beer.class))).thenReturn(testBeerDto);
        
        // When
        Page<BeerDto> result = beerService.getBeers("Test", null, pageable);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).beerName()).isEqualTo("Test Beer");
        verify(beerRepository).findByBeerNameContainingIgnoreCase(eq("Test"), any(Pageable.class));
    }
    
    @Test
    void getBeersWithoutNameFilter() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Beer> allBeersPage = new PageImpl<>(testBeerList, pageable, testBeerList.size());
        
        when(beerRepository.findAll(any(Pageable.class))).thenReturn(allBeersPage);
        when(beerMapper.beerToBeerDto(any(Beer.class))).thenReturn(testBeerDto);
        
        // When
        Page<BeerDto> result = beerService.getBeers(null, null, pageable);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        verify(beerRepository).findAll(any(Pageable.class));
    }
    
    @Test
    void getBeersWithEmptyNameFilter() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Beer> allBeersPage = new PageImpl<>(testBeerList, pageable, testBeerList.size());
        
        when(beerRepository.findAll(any(Pageable.class))).thenReturn(allBeersPage);
        when(beerMapper.beerToBeerDto(any(Beer.class))).thenReturn(testBeerDto);
        
        // When
        Page<BeerDto> result = beerService.getBeers("", null, pageable);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        verify(beerRepository).findAll(any(Pageable.class));
    }
    
    @Test
    void getBeersWithStyleFilter() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Beer> filteredBeerList = Arrays.asList(testBeer);
        Page<Beer> filteredBeerPage = new PageImpl<>(filteredBeerList, pageable, filteredBeerList.size());
        
        when(beerRepository.findByBeerStyleContainingIgnoreCase(eq("IPA"), any(Pageable.class)))
            .thenReturn(filteredBeerPage);
        when(beerMapper.beerToBeerDto(any(Beer.class))).thenReturn(testBeerDto);
        
        // When
        Page<BeerDto> result = beerService.getBeers(null, "IPA", pageable);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).beerStyle()).isEqualTo("IPA");
        verify(beerRepository).findByBeerStyleContainingIgnoreCase(eq("IPA"), any(Pageable.class));
    }
    
    @Test
    void getBeersWithEmptyStyleFilter() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Beer> allBeersPage = new PageImpl<>(testBeerList, pageable, testBeerList.size());
        
        when(beerRepository.findAll(any(Pageable.class))).thenReturn(allBeersPage);
        when(beerMapper.beerToBeerDto(any(Beer.class))).thenReturn(testBeerDto);
        
        // When
        Page<BeerDto> result = beerService.getBeers(null, "", pageable);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        verify(beerRepository).findAll(any(Pageable.class));
    }
    
    @Test
    void getBeersWithNameAndStyleFilter() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Beer> filteredBeerList = Arrays.asList(testBeer);
        Page<Beer> filteredBeerPage = new PageImpl<>(filteredBeerList, pageable, filteredBeerList.size());
        
        when(beerRepository.findByBeerNameContainingIgnoreCaseAndBeerStyleContainingIgnoreCase(
                eq("Test"), eq("IPA"), any(Pageable.class)))
            .thenReturn(filteredBeerPage);
        when(beerMapper.beerToBeerDto(any(Beer.class))).thenReturn(testBeerDto);
        
        // When
        Page<BeerDto> result = beerService.getBeers("Test", "IPA", pageable);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).beerName()).isEqualTo("Test Beer");
        assertThat(result.getContent().get(0).beerStyle()).isEqualTo("IPA");
        verify(beerRepository).findByBeerNameContainingIgnoreCaseAndBeerStyleContainingIgnoreCase(
                eq("Test"), eq("IPA"), any(Pageable.class));
    }
    
    @Test
    void patchBeerSuccess() {
        // Given
        Integer beerId = 1;
        BeerPatchDto patchDto = new BeerPatchDto(
            "Updated Name",
            null,  // beerStyle not changed
            "Updated description",
            null,  // upc not changed
            50,    // quantityOnHand changed
            null   // price not changed
        );
        
        when(beerRepository.findById(beerId)).thenReturn(Optional.of(testBeer));
        when(beerRepository.save(any(Beer.class))).thenReturn(testBeer);
        when(beerMapper.beerToBeerDto(any(Beer.class))).thenReturn(testBeerDto);
        
        // When
        Optional<BeerDto> result = beerService.patchBeer(beerId, patchDto);
        
        // Then
        assertThat(result).isPresent();
        verify(beerRepository).findById(beerId);
        verify(beerMapper).updateBeerFromPatchDto(eq(patchDto), any(Beer.class));
        verify(beerRepository).save(any(Beer.class));
    }
    
    @Test
    void patchBeerNotFound() {
        // Given
        Integer beerId = 999;
        BeerPatchDto patchDto = new BeerPatchDto(
            "Updated Name",
            null,
            null,
            null,
            null,
            null
        );
        
        when(beerRepository.findById(beerId)).thenReturn(Optional.empty());
        
        // When
        Optional<BeerDto> result = beerService.patchBeer(beerId, patchDto);
        
        // Then
        assertThat(result).isEmpty();
        verify(beerRepository).findById(beerId);
    }
}
