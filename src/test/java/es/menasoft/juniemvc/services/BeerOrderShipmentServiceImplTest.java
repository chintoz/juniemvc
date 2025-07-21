package es.menasoft.juniemvc.services;

import es.menasoft.juniemvc.entities.BeerOrder;
import es.menasoft.juniemvc.entities.BeerOrderShipment;
import es.menasoft.juniemvc.exceptions.EntityNotFoundException;
import es.menasoft.juniemvc.mappers.BeerOrderShipmentMapper;
import es.menasoft.juniemvc.models.BeerOrderShipmentDto;
import es.menasoft.juniemvc.models.CreateBeerOrderShipmentCommand;
import es.menasoft.juniemvc.repositories.BeerOrderRepository;
import es.menasoft.juniemvc.repositories.BeerOrderShipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeerOrderShipmentServiceImplTest {

    @Mock
    BeerOrderShipmentRepository beerOrderShipmentRepository;

    @Mock
    BeerOrderRepository beerOrderRepository;

    @Mock
    BeerOrderShipmentMapper beerOrderShipmentMapper;

    @InjectMocks
    BeerOrderShipmentServiceImpl beerOrderShipmentService;

    BeerOrder beerOrder;
    BeerOrderShipment beerOrderShipment;
    BeerOrderShipmentDto beerOrderShipmentDto;
    CreateBeerOrderShipmentCommand createCommand;

    @BeforeEach
    void setUp() {
        beerOrder = BeerOrder.builder()
                .id(1)
                .build();

        beerOrderShipment = BeerOrderShipment.builder()
                .id(1)
                .shipmentDate(LocalDate.now())
                .carrier("UPS")
                .trackingNumber("1234567890")
                .beerOrder(beerOrder)
                .build();

        beerOrderShipmentDto = new BeerOrderShipmentDto(
                1,
                null,
                LocalDate.now(),
                "UPS",
                "1234567890",
                LocalDateTime.now(),
                LocalDateTime.now(),
                1
        );

        createCommand = new CreateBeerOrderShipmentCommand(
                1,
                LocalDate.now(),
                "UPS",
                "1234567890"
        );
    }

    @Test
    void createShipment() {
        // given
        when(beerOrderRepository.findById(1)).thenReturn(Optional.of(beerOrder));
        when(beerOrderShipmentMapper.createBeerOrderShipmentCommandToBeerOrderShipment(any(), any())).thenReturn(beerOrderShipment);
        when(beerOrderShipmentRepository.save(any())).thenReturn(beerOrderShipment);
        when(beerOrderShipmentMapper.beerOrderShipmentToBeerOrderShipmentDto(any())).thenReturn(beerOrderShipmentDto);

        // when
        BeerOrderShipmentDto result = beerOrderShipmentService.createShipment(createCommand);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1);
        verify(beerOrderRepository).findById(1);
        verify(beerOrderShipmentMapper).createBeerOrderShipmentCommandToBeerOrderShipment(any(), any());
        verify(beerOrderShipmentRepository).save(any());
        verify(beerOrderShipmentMapper).beerOrderShipmentToBeerOrderShipmentDto(any());
    }

    @Test
    void createShipmentBeerOrderNotFound() {
        // given
        when(beerOrderRepository.findById(1)).thenReturn(Optional.empty());

        // when, then
        assertThrows(EntityNotFoundException.class, () -> beerOrderShipmentService.createShipment(createCommand));
        verify(beerOrderRepository).findById(1);
        verifyNoInteractions(beerOrderShipmentMapper);
        verifyNoInteractions(beerOrderShipmentRepository);
    }

    @Test
    void getShipmentById() {
        // given
        when(beerOrderShipmentRepository.findById(1)).thenReturn(Optional.of(beerOrderShipment));
        when(beerOrderShipmentMapper.beerOrderShipmentToBeerOrderShipmentDto(any())).thenReturn(beerOrderShipmentDto);

        // when
        Optional<BeerOrderShipmentDto> result = beerOrderShipmentService.getShipmentById(1);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(1);
        verify(beerOrderShipmentRepository).findById(1);
        verify(beerOrderShipmentMapper).beerOrderShipmentToBeerOrderShipmentDto(any());
    }

    @Test
    void getShipmentByIdNotFound() {
        // given
        when(beerOrderShipmentRepository.findById(1)).thenReturn(Optional.empty());

        // when
        Optional<BeerOrderShipmentDto> result = beerOrderShipmentService.getShipmentById(1);

        // then
        assertThat(result).isEmpty();
        verify(beerOrderShipmentRepository).findById(1);
        verifyNoInteractions(beerOrderShipmentMapper);
    }

    @Test
    void getAllShipments() {
        // given
        List<BeerOrderShipment> shipments = Arrays.asList(beerOrderShipment);
        when(beerOrderShipmentRepository.findAll()).thenReturn(shipments);
        when(beerOrderShipmentMapper.beerOrderShipmentToBeerOrderShipmentDto(any())).thenReturn(beerOrderShipmentDto);

        // when
        List<BeerOrderShipmentDto> result = beerOrderShipmentService.getAllShipments();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(1);
        verify(beerOrderShipmentRepository).findAll();
        verify(beerOrderShipmentMapper, times(1)).beerOrderShipmentToBeerOrderShipmentDto(any());
    }

    @Test
    void getShipmentsByBeerOrderId() {
        // given
        List<BeerOrderShipment> shipments = Arrays.asList(beerOrderShipment);
        when(beerOrderShipmentRepository.findAllByBeerOrderId(1)).thenReturn(shipments);
        when(beerOrderShipmentMapper.beerOrderShipmentToBeerOrderShipmentDto(any())).thenReturn(beerOrderShipmentDto);

        // when
        List<BeerOrderShipmentDto> result = beerOrderShipmentService.getShipmentsByBeerOrderId(1);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(1);
        verify(beerOrderShipmentRepository).findAllByBeerOrderId(1);
        verify(beerOrderShipmentMapper, times(1)).beerOrderShipmentToBeerOrderShipmentDto(any());
    }

    @Test
    void updateShipment() {
        // given
        when(beerOrderShipmentRepository.findById(1)).thenReturn(Optional.of(beerOrderShipment));
        when(beerOrderShipmentRepository.save(any())).thenReturn(beerOrderShipment);
        when(beerOrderShipmentMapper.beerOrderShipmentToBeerOrderShipmentDto(any())).thenReturn(beerOrderShipmentDto);

        // when
        Optional<BeerOrderShipmentDto> result = beerOrderShipmentService.updateShipment(1, beerOrderShipmentDto);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(1);
        verify(beerOrderShipmentRepository).findById(1);
        verify(beerOrderShipmentRepository).save(any());
        verify(beerOrderShipmentMapper).beerOrderShipmentToBeerOrderShipmentDto(any());
    }

    @Test
    void updateShipmentNotFound() {
        // given
        when(beerOrderShipmentRepository.findById(1)).thenReturn(Optional.empty());

        // when
        Optional<BeerOrderShipmentDto> result = beerOrderShipmentService.updateShipment(1, beerOrderShipmentDto);

        // then
        assertThat(result).isEmpty();
        verify(beerOrderShipmentRepository).findById(1);
        verifyNoMoreInteractions(beerOrderShipmentRepository);
        verifyNoInteractions(beerOrderShipmentMapper);
    }

    @Test
    void deleteShipment() {
        // given
        when(beerOrderShipmentRepository.findById(1)).thenReturn(Optional.of(beerOrderShipment));

        // when
        boolean result = beerOrderShipmentService.deleteShipment(1);

        // then
        assertThat(result).isTrue();
        verify(beerOrderShipmentRepository).findById(1);
        verify(beerOrderShipmentRepository).delete(any());
    }

    @Test
    void deleteShipmentNotFound() {
        // given
        when(beerOrderShipmentRepository.findById(1)).thenReturn(Optional.empty());

        // when
        boolean result = beerOrderShipmentService.deleteShipment(1);

        // then
        assertThat(result).isFalse();
        verify(beerOrderShipmentRepository).findById(1);
        verifyNoMoreInteractions(beerOrderShipmentRepository);
    }
}