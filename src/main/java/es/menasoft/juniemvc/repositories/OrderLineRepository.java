package es.menasoft.juniemvc.repositories;

import es.menasoft.juniemvc.entities.Beer;
import es.menasoft.juniemvc.entities.BeerOrder;
import es.menasoft.juniemvc.entities.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Integer> {
    // Find order lines by beer
    List<OrderLine> findAllByBeer(Beer beer);
    
    // Find order lines by beer ID
    List<OrderLine> findAllByBeerId(Integer beerId);
    
    // Find order lines by order
    List<OrderLine> findAllByBeerOrder(BeerOrder beerOrder);
    
    // Find order lines by order ID
    List<OrderLine> findAllByBeerOrderId(Integer beerOrderId);
    
    // Find order lines by beer and order
    List<OrderLine> findAllByBeerAndBeerOrder(Beer beer, BeerOrder beerOrder);
    
    // Find order lines by beer ID and order ID
    List<OrderLine> findAllByBeerIdAndBeerOrderId(Integer beerId, Integer beerOrderId);
}