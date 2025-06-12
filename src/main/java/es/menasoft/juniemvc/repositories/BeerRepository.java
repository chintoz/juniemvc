package es.menasoft.juniemvc.repositories;

import es.menasoft.juniemvc.entities.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeerRepository extends JpaRepository<Beer, Integer> {
    // Spring Data JPA will automatically implement basic CRUD operations
    // We can add custom query methods here if needed
}