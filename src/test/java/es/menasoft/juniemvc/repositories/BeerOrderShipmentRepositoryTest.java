package es.menasoft.juniemvc.repositories;

import es.menasoft.juniemvc.entities.BeerOrder;
import es.menasoft.juniemvc.entities.BeerOrderShipment;
import es.menasoft.juniemvc.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class BeerOrderShipmentRepositoryTest {

    @Autowired
    BeerOrderShipmentRepository beerOrderShipmentRepository;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    CustomerRepository customerRepository;

    Customer customer;
    BeerOrder beerOrder;
    BeerOrderShipment shipment1;
    BeerOrderShipment shipment2;

    @BeforeEach
    void setUp() {
        // Create and save a customer
        customer = Customer.builder()
                .name("Test Customer")
                .email("test@example.com")
                .addressLine1("123 Test St")
                .city("Test City")
                .state("Test State")
                .postalCode("12345")
                .build();
        customer = customerRepository.save(customer);

        // Create and save a beer order
        beerOrder = BeerOrder.builder()
                .orderStatus("NEW")
                .customer(customer)
                .build();
        beerOrder = beerOrderRepository.save(beerOrder);

        // Create and save shipments
        shipment1 = BeerOrderShipment.builder()
                .shipmentDate(LocalDate.now())
                .carrier("UPS")
                .trackingNumber("1234567890")
                .beerOrder(beerOrder)
                .build();
        shipment1 = beerOrderShipmentRepository.save(shipment1);

        shipment2 = BeerOrderShipment.builder()
                .shipmentDate(LocalDate.now().plusDays(1))
                .carrier("FedEx")
                .trackingNumber("0987654321")
                .beerOrder(beerOrder)
                .build();
        shipment2 = beerOrderShipmentRepository.save(shipment2);
    }

    @Test
    void testSaveAndFindById() {
        // given
        BeerOrderShipment shipment = BeerOrderShipment.builder()
                .shipmentDate(LocalDate.now().plusDays(2))
                .carrier("DHL")
                .trackingNumber("5555555555")
                .beerOrder(beerOrder)
                .build();

        // when
        BeerOrderShipment savedShipment = beerOrderShipmentRepository.save(shipment);
        BeerOrderShipment foundShipment = beerOrderShipmentRepository.findById(savedShipment.getId()).orElse(null);

        // then
        assertThat(foundShipment).isNotNull();
        assertThat(foundShipment.getId()).isEqualTo(savedShipment.getId());
        assertThat(foundShipment.getCarrier()).isEqualTo("DHL");
        assertThat(foundShipment.getTrackingNumber()).isEqualTo("5555555555");
        assertThat(foundShipment.getBeerOrder().getId()).isEqualTo(beerOrder.getId());
    }

    @Test
    void testFindAllByBeerOrder() {
        // when
        List<BeerOrderShipment> shipments = beerOrderShipmentRepository.findAllByBeerOrder(beerOrder);

        // then
        assertThat(shipments).hasSize(2);
        assertThat(shipments).contains(shipment1, shipment2);
    }

    @Test
    void testFindAllByBeerOrderId() {
        // when
        List<BeerOrderShipment> shipments = beerOrderShipmentRepository.findAllByBeerOrderId(beerOrder.getId());

        // then
        assertThat(shipments).hasSize(2);
        assertThat(shipments).contains(shipment1, shipment2);
    }

    @Test
    void testDeleteShipment() {
        // when
        beerOrderShipmentRepository.delete(shipment1);
        List<BeerOrderShipment> shipments = beerOrderShipmentRepository.findAllByBeerOrder(beerOrder);

        // then
        assertThat(shipments).hasSize(1);
        assertThat(shipments).contains(shipment2);
        assertThat(shipments).doesNotContain(shipment1);
    }
}