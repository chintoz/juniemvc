package es.menasoft.juniemvc.repositories;

import es.menasoft.juniemvc.entities.BeerOrder;
import es.menasoft.juniemvc.entities.BeerOrderShipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeerOrderShipmentRepository extends JpaRepository<BeerOrderShipment, Integer> {
    // Find shipments by beer order
    List<BeerOrderShipment> findAllByBeerOrder(BeerOrder beerOrder);
    
    // Find shipments by beer order ID
    List<BeerOrderShipment> findAllByBeerOrderId(Integer beerOrderId);
}