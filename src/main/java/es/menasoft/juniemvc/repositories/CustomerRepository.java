package es.menasoft.juniemvc.repositories;

import es.menasoft.juniemvc.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    // Spring Data JPA will automatically implement basic CRUD operations
}