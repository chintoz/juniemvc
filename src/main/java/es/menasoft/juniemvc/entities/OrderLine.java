package es.menasoft.juniemvc.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer orderQuantity;

    @ManyToOne
    private Beer beer;

    @ManyToOne
    private BeerOrder beerOrder;
}