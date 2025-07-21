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
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class BeerOrderRepositoryTest {

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerRepository beerRepository;

    private Customer testCustomer;
    private Beer testBeer;

    @BeforeEach
    public void setUp() {
        // Create and save a test customer
        testCustomer = Customer.builder()
                .name("Test Customer")
                .email("test@example.com")
                .phone("123-456-7890")
                .addressLine1("123 Test St")
                .addressLine2("Apt 456")
                .city("Test City")
                .state("Test State")
                .postalCode("12345")
                .build();
        testCustomer = customerRepository.save(testCustomer);

        // Create and save a test beer
        testBeer = Beer.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
        testBeer = beerRepository.save(testBeer);
    }

    @Test
    public void testSaveBeerOrder() {
        BeerOrder beerOrder = BeerOrder.builder()
                .orderStatus("NEW")
                .customer(testCustomer)
                .build();

        // Add an order line
        OrderLine orderLine = OrderLine.builder()
                .orderQuantity(5)
                .beer(testBeer)
                .build();
        beerOrder.addOrderLine(orderLine);

        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);

        assertThat(savedBeerOrder).isNotNull();
        assertThat(savedBeerOrder.getId()).isNotNull();
        assertThat(savedBeerOrder.getOrderLines()).hasSize(1);
    }

    @Test
    public void testGetBeerOrderById() {
        BeerOrder beerOrder = BeerOrder.builder()
                .orderStatus("NEW")
                .customer(testCustomer)
                .build();

        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);

        Optional<BeerOrder> fetchedBeerOrderOptional = beerOrderRepository.findById(savedBeerOrder.getId());

        assertThat(fetchedBeerOrderOptional).isPresent();
        BeerOrder fetchedBeerOrder = fetchedBeerOrderOptional.get();
        assertThat(fetchedBeerOrder.getOrderStatus()).isEqualTo("NEW");
        assertThat(fetchedBeerOrder.getCustomer().getId()).isEqualTo(testCustomer.getId());
    }

    @Test
    public void testFindAllByCustomer() {
        // Create and save two beer orders for the test customer
        BeerOrder beerOrder1 = BeerOrder.builder()
                .orderStatus("NEW")
                .customer(testCustomer)
                .build();
        beerOrderRepository.save(beerOrder1);

        BeerOrder beerOrder2 = BeerOrder.builder()
                .orderStatus("PROCESSING")
                .customer(testCustomer)
                .build();
        beerOrderRepository.save(beerOrder2);

        // Create another customer and order
        Customer anotherCustomer = Customer.builder()
                .name("Another Customer")
                .email("another@example.com")
                .phone("987-654-3210")
                .addressLine1("456 Another St")
                .addressLine2("Suite 789")
                .city("Another City")
                .state("Another State")
                .postalCode("54321")
                .build();
        anotherCustomer = customerRepository.save(anotherCustomer);

        BeerOrder beerOrder3 = BeerOrder.builder()
                .orderStatus("NEW")
                .customer(anotherCustomer)
                .build();
        beerOrderRepository.save(beerOrder3);

        // Test findAllByCustomer
        List<BeerOrder> customerOrders = beerOrderRepository.findAllByCustomer(testCustomer);

        assertThat(customerOrders).isNotNull();
        assertThat(customerOrders).hasSize(2);
        assertThat(customerOrders.get(0).getCustomer().getId()).isEqualTo(testCustomer.getId());
        assertThat(customerOrders.get(1).getCustomer().getId()).isEqualTo(testCustomer.getId());
    }

    @Test
    public void testFindAllByCustomerId() {
        // Create and save two beer orders for the test customer
        BeerOrder beerOrder1 = BeerOrder.builder()
                .orderStatus("NEW")
                .customer(testCustomer)
                .build();
        beerOrderRepository.save(beerOrder1);

        BeerOrder beerOrder2 = BeerOrder.builder()
                .orderStatus("PROCESSING")
                .customer(testCustomer)
                .build();
        beerOrderRepository.save(beerOrder2);

        // Create another customer and order
        Customer anotherCustomer = Customer.builder()
                .name("Another Customer")
                .email("another@example.com")
                .phone("987-654-3210")
                .addressLine1("456 Another St")
                .addressLine2("Suite 789")
                .city("Another City")
                .state("Another State")
                .postalCode("54321")
                .build();
        anotherCustomer = customerRepository.save(anotherCustomer);

        BeerOrder beerOrder3 = BeerOrder.builder()
                .orderStatus("NEW")
                .customer(anotherCustomer)
                .build();
        beerOrderRepository.save(beerOrder3);

        // Test findAllByCustomerId
        List<BeerOrder> customerOrders = beerOrderRepository.findAllByCustomerId(testCustomer.getId());

        assertThat(customerOrders).isNotNull();
        assertThat(customerOrders).hasSize(2);
        assertThat(customerOrders.get(0).getCustomer().getId()).isEqualTo(testCustomer.getId());
        assertThat(customerOrders.get(1).getCustomer().getId()).isEqualTo(testCustomer.getId());
    }

    @Test
    @Rollback
    public void testUpdateBeerOrder() {
        BeerOrder beerOrder = BeerOrder.builder()
                .orderStatus("NEW")
                .customer(testCustomer)
                .build();

        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);
        
        savedBeerOrder.setOrderStatus("PROCESSING");
        BeerOrder updatedBeerOrder = beerOrderRepository.save(savedBeerOrder);
        
        assertThat(updatedBeerOrder.getOrderStatus()).isEqualTo("PROCESSING");
    }

    @Test
    @Rollback
    public void testDeleteBeerOrder() {
        BeerOrder beerOrder = BeerOrder.builder()
                .orderStatus("NEW")
                .customer(testCustomer)
                .build();

        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);
        
        beerOrderRepository.deleteById(savedBeerOrder.getId());
        
        Optional<BeerOrder> deletedBeerOrder = beerOrderRepository.findById(savedBeerOrder.getId());
        
        assertThat(deletedBeerOrder).isEmpty();
    }
}