package es.menasoft.juniemvc.services;

import es.menasoft.juniemvc.entities.Beer;
import es.menasoft.juniemvc.entities.BeerOrder;
import es.menasoft.juniemvc.entities.Customer;
import es.menasoft.juniemvc.entities.OrderLine;
import es.menasoft.juniemvc.mappers.BeerOrderMapper;
import es.menasoft.juniemvc.mappers.OrderLineMapper;
import es.menasoft.juniemvc.models.BeerOrderDto;
import es.menasoft.juniemvc.models.CreateBeerOrderCommand;
import es.menasoft.juniemvc.models.OrderLineDto;
import es.menasoft.juniemvc.repositories.BeerOrderRepository;
import es.menasoft.juniemvc.repositories.BeerRepository;
import es.menasoft.juniemvc.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeerOrderServiceImplTest {

    @Mock
    private BeerOrderRepository beerOrderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BeerRepository beerRepository;

    @Mock
    private BeerOrderMapper beerOrderMapper;

    @Mock
    private OrderLineMapper orderLineMapper;

    @InjectMocks
    private BeerOrderServiceImpl beerOrderService;

    private Customer testCustomer;
    private Beer testBeer;
    private BeerOrder testBeerOrder;
    private OrderLine testOrderLine;
    private BeerOrderDto testBeerOrderDto;
    private OrderLineDto testOrderLineDto;
    private List<BeerOrder> testBeerOrderList;
    private List<BeerOrderDto> testBeerOrderDtoList;

    @BeforeEach
    void setUp() {
        // Setup test Customer
        testCustomer = Customer.builder()
                .id(1)
                .name("Test Customer")
                .email("test@example.com")
                .phone("123-456-7890")
                .build();

        // Setup test Beer
        testBeer = Beer.builder()
                .id(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456789")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        // Setup test OrderLine
        testOrderLine = OrderLine.builder()
                .id(1)
                .orderQuantity(5)
                .beer(testBeer)
                .build();

        // Setup test BeerOrder
        testBeerOrder = BeerOrder.builder()
                .id(1)
                .orderStatus("NEW")
                .customer(testCustomer)
                .build();
        testBeerOrder.addOrderLine(testOrderLine);

        // Setup another test BeerOrder
        BeerOrder testBeerOrder2 = BeerOrder.builder()
                .id(2)
                .orderStatus("PROCESSING")
                .customer(testCustomer)
                .build();

        testBeerOrderList = Arrays.asList(testBeerOrder, testBeerOrder2);

        // Setup test OrderLineDto
        testOrderLineDto = new OrderLineDto(
                1,
                5,
                1,
                "Test Beer"
        );

        // Setup test BeerOrderDto
        testBeerOrderDto = new BeerOrderDto(
                1,
                null,
                "NEW",
                null,
                null,
                1,
                List.of(testOrderLineDto)
        );

        BeerOrderDto testBeerOrderDto2 = new BeerOrderDto(
                2,
                null,
                "PROCESSING",
                null,
                null,
                1,
                Collections.emptyList()
        );

        testBeerOrderDtoList = Arrays.asList(testBeerOrderDto, testBeerOrderDto2);
    }

    @Test
    void createBeerOrder() {
        // Given
        CreateBeerOrderCommand command = new CreateBeerOrderCommand(
                1,
                List.of(new OrderLineDto(null, 5, 1, null))
        );

        when(customerRepository.findById(1)).thenReturn(Optional.of(testCustomer));
        when(beerRepository.findById(1)).thenReturn(Optional.of(testBeer));
        when(beerOrderMapper.createBeerOrderCommandToBeerOrder(command, testCustomer)).thenReturn(testBeerOrder);
        when(orderLineMapper.orderLineDtoToOrderLine(any(OrderLineDto.class), eq(testBeer), eq(testBeerOrder))).thenReturn(testOrderLine);
        when(beerOrderRepository.save(any(BeerOrder.class))).thenReturn(testBeerOrder);
        when(beerOrderMapper.beerOrderToBeerOrderDto(testBeerOrder)).thenReturn(testBeerOrderDto);

        // When
        BeerOrderDto result = beerOrderService.createBeerOrder(command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1);
        assertThat(result.orderStatus()).isEqualTo("NEW");
        assertThat(result.customerId()).isEqualTo(1);
        assertThat(result.orderLines()).hasSize(1);
        verify(customerRepository).findById(1);
        verify(beerRepository).findById(1);
        verify(beerOrderMapper).createBeerOrderCommandToBeerOrder(command, testCustomer);
        verify(orderLineMapper).orderLineDtoToOrderLine(any(OrderLineDto.class), eq(testBeer), eq(testBeerOrder));
        verify(beerOrderRepository).save(any(BeerOrder.class));
        verify(beerOrderMapper).beerOrderToBeerOrderDto(testBeerOrder);
    }

    @Test
    void createBeerOrderCustomerNotFound() {
        // Given
        CreateBeerOrderCommand command = new CreateBeerOrderCommand(
                999,
                List.of(new OrderLineDto(null, 5, 1, null))
        );

        when(customerRepository.findById(999)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> beerOrderService.createBeerOrder(command))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Customer not found with ID: 999");
        verify(customerRepository).findById(999);
    }

    @Test
    void createBeerOrderBeerNotFound() {
        // Given
        CreateBeerOrderCommand command = new CreateBeerOrderCommand(
                1,
                List.of(new OrderLineDto(null, 5, 999, null))
        );

        when(customerRepository.findById(1)).thenReturn(Optional.of(testCustomer));
        when(beerRepository.findById(999)).thenReturn(Optional.empty());
        when(beerOrderMapper.createBeerOrderCommandToBeerOrder(command, testCustomer)).thenReturn(testBeerOrder);

        // When/Then
        assertThatThrownBy(() -> beerOrderService.createBeerOrder(command))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Beer not found with ID: 999");
        verify(customerRepository).findById(1);
        verify(beerRepository).findById(999);
        verify(beerOrderMapper).createBeerOrderCommandToBeerOrder(command, testCustomer);
    }

    @Test
    void getBeerOrderById() {
        // Given
        when(beerOrderRepository.findById(1)).thenReturn(Optional.of(testBeerOrder));
        when(beerOrderMapper.beerOrderToBeerOrderDto(testBeerOrder)).thenReturn(testBeerOrderDto);

        // When
        Optional<BeerOrderDto> result = beerOrderService.getBeerOrderById(1);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(1);
        assertThat(result.get().orderStatus()).isEqualTo("NEW");
        verify(beerOrderRepository).findById(1);
        verify(beerOrderMapper).beerOrderToBeerOrderDto(testBeerOrder);
    }

    @Test
    void getBeerOrderByIdNotFound() {
        // Given
        when(beerOrderRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<BeerOrderDto> result = beerOrderService.getBeerOrderById(999);

        // Then
        assertThat(result).isEmpty();
        verify(beerOrderRepository).findById(999);
    }

    @Test
    void getAllBeerOrders() {
        // Given
        when(beerOrderRepository.findAll()).thenReturn(testBeerOrderList);
        when(beerOrderMapper.beerOrderToBeerOrderDto(testBeerOrder)).thenReturn(testBeerOrderDto);
        when(beerOrderMapper.beerOrderToBeerOrderDto(testBeerOrderList.get(1))).thenReturn(testBeerOrderDtoList.get(1));

        // When
        List<BeerOrderDto> result = beerOrderService.getAllBeerOrders();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(1);
        assertThat(result.get(0).orderStatus()).isEqualTo("NEW");
        assertThat(result.get(1).id()).isEqualTo(2);
        assertThat(result.get(1).orderStatus()).isEqualTo("PROCESSING");
        verify(beerOrderRepository).findAll();
        verify(beerOrderMapper).beerOrderToBeerOrderDto(testBeerOrder);
        verify(beerOrderMapper).beerOrderToBeerOrderDto(testBeerOrderList.get(1));
    }

    @Test
    void getBeerOrdersByCustomerId() {
        // Given
        when(beerOrderRepository.findAllByCustomerId(1)).thenReturn(testBeerOrderList);
        when(beerOrderMapper.beerOrderToBeerOrderDto(testBeerOrder)).thenReturn(testBeerOrderDto);
        when(beerOrderMapper.beerOrderToBeerOrderDto(testBeerOrderList.get(1))).thenReturn(testBeerOrderDtoList.get(1));

        // When
        List<BeerOrderDto> result = beerOrderService.getBeerOrdersByCustomerId(1);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(1);
        assertThat(result.get(0).orderStatus()).isEqualTo("NEW");
        assertThat(result.get(1).id()).isEqualTo(2);
        assertThat(result.get(1).orderStatus()).isEqualTo("PROCESSING");
        verify(beerOrderRepository).findAllByCustomerId(1);
        verify(beerOrderMapper).beerOrderToBeerOrderDto(testBeerOrder);
        verify(beerOrderMapper).beerOrderToBeerOrderDto(testBeerOrderList.get(1));
    }

    @Test
    void updateBeerOrder() {
        // Given
        BeerOrderDto beerOrderDtoToUpdate = new BeerOrderDto(
                null,
                null,
                "COMPLETED",
                null,
                null,
                1,
                List.of(testOrderLineDto)
        );

        when(beerOrderRepository.findById(1)).thenReturn(Optional.of(testBeerOrder));
        when(beerOrderRepository.save(any(BeerOrder.class))).thenReturn(testBeerOrder);
        when(beerOrderMapper.beerOrderToBeerOrderDto(testBeerOrder)).thenReturn(testBeerOrderDto);

        // When
        Optional<BeerOrderDto> result = beerOrderService.updateBeerOrder(1, beerOrderDtoToUpdate);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(1);
        verify(beerOrderRepository).findById(1);
        verify(beerOrderRepository).save(any(BeerOrder.class));
        verify(beerOrderMapper).beerOrderToBeerOrderDto(testBeerOrder);
    }

    @Test
    void updateBeerOrderNotFound() {
        // Given
        BeerOrderDto beerOrderDtoToUpdate = new BeerOrderDto(
                null,
                null,
                "COMPLETED",
                null,
                null,
                1,
                List.of(testOrderLineDto)
        );

        when(beerOrderRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<BeerOrderDto> result = beerOrderService.updateBeerOrder(999, beerOrderDtoToUpdate);

        // Then
        assertThat(result).isEmpty();
        verify(beerOrderRepository).findById(999);
    }

    @Test
    void updateBeerOrderStatus() {
        // Given
        when(beerOrderRepository.findById(1)).thenReturn(Optional.of(testBeerOrder));
        when(beerOrderRepository.save(any(BeerOrder.class))).thenReturn(testBeerOrder);
        when(beerOrderMapper.beerOrderToBeerOrderDto(testBeerOrder)).thenReturn(testBeerOrderDto);

        // When
        Optional<BeerOrderDto> result = beerOrderService.updateBeerOrderStatus(1, "COMPLETED");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(1);
        verify(beerOrderRepository).findById(1);
        verify(beerOrderRepository).save(any(BeerOrder.class));
        verify(beerOrderMapper).beerOrderToBeerOrderDto(testBeerOrder);
    }

    @Test
    void updateBeerOrderStatusNotFound() {
        // Given
        when(beerOrderRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<BeerOrderDto> result = beerOrderService.updateBeerOrderStatus(999, "COMPLETED");

        // Then
        assertThat(result).isEmpty();
        verify(beerOrderRepository).findById(999);
    }

    @Test
    void deleteBeerOrder() {
        // Given
        when(beerOrderRepository.findById(1)).thenReturn(Optional.of(testBeerOrder));

        // When
        boolean result = beerOrderService.deleteBeerOrder(1);

        // Then
        assertThat(result).isTrue();
        verify(beerOrderRepository).findById(1);
        verify(beerOrderRepository).delete(testBeerOrder);
    }

    @Test
    void deleteBeerOrderNotFound() {
        // Given
        when(beerOrderRepository.findById(999)).thenReturn(Optional.empty());

        // When
        boolean result = beerOrderService.deleteBeerOrder(999);

        // Then
        assertThat(result).isFalse();
        verify(beerOrderRepository).findById(999);
    }
}