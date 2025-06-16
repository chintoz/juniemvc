package es.menasoft.juniemvc.repositories;

import es.menasoft.juniemvc.entities.Beer;
import es.menasoft.juniemvc.entities.BeerOrder;
import es.menasoft.juniemvc.entities.Customer;
import es.menasoft.juniemvc.entities.OrderLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderLineRepositoryTest {

    @Autowired
    OrderLineRepository orderLineRepository;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerRepository beerRepository;

    private Customer testCustomer;
    private Beer testBeer1;
    private Beer testBeer2;
    private BeerOrder testBeerOrder1;
    private BeerOrder testBeerOrder2;
    private OrderLine testOrderLine1;
    private OrderLine testOrderLine2;
    private OrderLine testOrderLine3;

    @BeforeEach
    void setUp() {
        // Create and save a test customer
        testCustomer = Customer.builder()
                .name("Test Customer")
                .email("test@example.com")
                .phone("123-456-7890")
                .build();
        testCustomer = customerRepository.save(testCustomer);

        // Create and save test beers
        testBeer1 = Beer.builder()
                .beerName("Test Beer 1")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
        testBeer1 = beerRepository.save(testBeer1);

        testBeer2 = Beer.builder()
                .beerName("Test Beer 2")
                .beerStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(50)
                .build();
        testBeer2 = beerRepository.save(testBeer2);

        // Create and save test beer orders
        testBeerOrder1 = BeerOrder.builder()
                .orderStatus("NEW")
                .customer(testCustomer)
                .build();
        testBeerOrder1 = beerOrderRepository.save(testBeerOrder1);

        testBeerOrder2 = BeerOrder.builder()
                .orderStatus("PROCESSING")
                .customer(testCustomer)
                .build();
        testBeerOrder2 = beerOrderRepository.save(testBeerOrder2);

        // Create and save test order lines
        testOrderLine1 = OrderLine.builder()
                .orderQuantity(5)
                .beer(testBeer1)
                .beerOrder(testBeerOrder1)
                .build();
        testOrderLine1 = orderLineRepository.save(testOrderLine1);

        testOrderLine2 = OrderLine.builder()
                .orderQuantity(3)
                .beer(testBeer2)
                .beerOrder(testBeerOrder1)
                .build();
        testOrderLine2 = orderLineRepository.save(testOrderLine2);

        testOrderLine3 = OrderLine.builder()
                .orderQuantity(10)
                .beer(testBeer1)
                .beerOrder(testBeerOrder2)
                .build();
        testOrderLine3 = orderLineRepository.save(testOrderLine3);
    }

    @Test
    void testSaveOrderLine() {
        OrderLine orderLine = OrderLine.builder()
                .orderQuantity(2)
                .beer(testBeer2)
                .beerOrder(testBeerOrder2)
                .build();

        OrderLine savedOrderLine = orderLineRepository.save(orderLine);

        assertThat(savedOrderLine).isNotNull();
        assertThat(savedOrderLine.getId()).isNotNull();
    }

    @Test
    void testGetOrderLineById() {
        Optional<OrderLine> fetchedOrderLineOptional = orderLineRepository.findById(testOrderLine1.getId());

        assertThat(fetchedOrderLineOptional).isPresent();
        OrderLine fetchedOrderLine = fetchedOrderLineOptional.get();
        assertThat(fetchedOrderLine.getOrderQuantity()).isEqualTo(5);
        assertThat(fetchedOrderLine.getBeer().getId()).isEqualTo(testBeer1.getId());
        assertThat(fetchedOrderLine.getBeerOrder().getId()).isEqualTo(testBeerOrder1.getId());
    }

    @Test
    void testFindAllByBeer() {
        List<OrderLine> orderLines = orderLineRepository.findAllByBeer(testBeer1);

        assertThat(orderLines).isNotNull();
        assertThat(orderLines).hasSize(2);
        assertThat(orderLines.get(0).getBeer().getId()).isEqualTo(testBeer1.getId());
        assertThat(orderLines.get(1).getBeer().getId()).isEqualTo(testBeer1.getId());
    }

    @Test
    void testFindAllByBeerId() {
        List<OrderLine> orderLines = orderLineRepository.findAllByBeerId(testBeer1.getId());

        assertThat(orderLines).isNotNull();
        assertThat(orderLines).hasSize(2);
        assertThat(orderLines.get(0).getBeer().getId()).isEqualTo(testBeer1.getId());
        assertThat(orderLines.get(1).getBeer().getId()).isEqualTo(testBeer1.getId());
    }

    @Test
    void testFindAllByBeerOrder() {
        List<OrderLine> orderLines = orderLineRepository.findAllByBeerOrder(testBeerOrder1);

        assertThat(orderLines).isNotNull();
        assertThat(orderLines).hasSize(2);
        assertThat(orderLines.get(0).getBeerOrder().getId()).isEqualTo(testBeerOrder1.getId());
        assertThat(orderLines.get(1).getBeerOrder().getId()).isEqualTo(testBeerOrder1.getId());
    }

    @Test
    void testFindAllByBeerOrderId() {
        List<OrderLine> orderLines = orderLineRepository.findAllByBeerOrderId(testBeerOrder1.getId());

        assertThat(orderLines).isNotNull();
        assertThat(orderLines).hasSize(2);
        assertThat(orderLines.get(0).getBeerOrder().getId()).isEqualTo(testBeerOrder1.getId());
        assertThat(orderLines.get(1).getBeerOrder().getId()).isEqualTo(testBeerOrder1.getId());
    }

    @Test
    void testFindAllByBeerAndBeerOrder() {
        List<OrderLine> orderLines = orderLineRepository.findAllByBeerAndBeerOrder(testBeer1, testBeerOrder1);

        assertThat(orderLines).isNotNull();
        assertThat(orderLines).hasSize(1);
        assertThat(orderLines.get(0).getBeer().getId()).isEqualTo(testBeer1.getId());
        assertThat(orderLines.get(0).getBeerOrder().getId()).isEqualTo(testBeerOrder1.getId());
    }

    @Test
    void testFindAllByBeerIdAndBeerOrderId() {
        List<OrderLine> orderLines = orderLineRepository.findAllByBeerIdAndBeerOrderId(testBeer1.getId(), testBeerOrder1.getId());

        assertThat(orderLines).isNotNull();
        assertThat(orderLines).hasSize(1);
        assertThat(orderLines.get(0).getBeer().getId()).isEqualTo(testBeer1.getId());
        assertThat(orderLines.get(0).getBeerOrder().getId()).isEqualTo(testBeerOrder1.getId());
    }

    @Test
    @Rollback
    void testUpdateOrderLine() {
        testOrderLine1.setOrderQuantity(15);
        OrderLine updatedOrderLine = orderLineRepository.save(testOrderLine1);
        
        assertThat(updatedOrderLine.getOrderQuantity()).isEqualTo(15);
    }

    @Test
    @Rollback
    void testDeleteOrderLine() {
        orderLineRepository.deleteById(testOrderLine1.getId());
        
        Optional<OrderLine> deletedOrderLine = orderLineRepository.findById(testOrderLine1.getId());
        
        assertThat(deletedOrderLine).isEmpty();
    }
}