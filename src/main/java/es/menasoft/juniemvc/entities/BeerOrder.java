package es.menasoft.juniemvc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BeerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    private String orderStatus;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "beerOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private Set<OrderLine> orderLines = new HashSet<>();
    
    @OneToMany(mappedBy = "beerOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private Set<BeerOrderShipment> shipments = new HashSet<>();

    /**
     * Helper method to add an order line to this order and set the order on the order line
     * @param orderLine the order line to add
     * @return the order line that was added
     */
    public OrderLine addOrderLine(OrderLine orderLine) {
        orderLines.add(orderLine);
        orderLine.setBeerOrder(this);
        return orderLine;
    }

    /**
     * Helper method to remove an order line from this order and set the order on the order line to null
     * @param orderLine the order line to remove
     * @return the order line that was removed
     */
    public OrderLine removeOrderLine(OrderLine orderLine) {
        orderLines.remove(orderLine);
        orderLine.setBeerOrder(null);
        return orderLine;
    }
    
    /**
     * Helper method to add a shipment to this order and set the order on the shipment
     * @param shipment the shipment to add
     * @return the shipment that was added
     */
    public BeerOrderShipment addShipment(BeerOrderShipment shipment) {
        shipments.add(shipment);
        shipment.setBeerOrder(this);
        return shipment;
    }

    /**
     * Helper method to remove a shipment from this order and set the order on the shipment to null
     * @param shipment the shipment to remove
     * @return the shipment that was removed
     */
    public BeerOrderShipment removeShipment(BeerOrderShipment shipment) {
        shipments.remove(shipment);
        shipment.setBeerOrder(null);
        return shipment;
    }
}