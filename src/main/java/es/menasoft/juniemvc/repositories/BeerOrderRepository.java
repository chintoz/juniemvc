package es.menasoft.juniemvc.repositories;

import es.menasoft.juniemvc.entities.BeerOrder;
import es.menasoft.juniemvc.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeerOrderRepository extends JpaRepository<BeerOrder, Integer> {
    // Find orders by customer
    List<BeerOrder> findAllByCustomer(Customer customer);
    
    // Find orders by customer ID
    List<BeerOrder> findAllByCustomerId(Integer customerId);
}