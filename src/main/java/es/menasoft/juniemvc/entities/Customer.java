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
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    private String name;
    private String email;
    private String phone;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private Set<BeerOrder> orders = new HashSet<>();

    /**
     * Helper method to add an order to this customer and set the customer on the order
     * @param order the order to add
     * @return the order that was added
     */
    public BeerOrder addOrder(BeerOrder order) {
        orders.add(order);
        order.setCustomer(this);
        return order;
    }

    /**
     * Helper method to remove an order from this customer and set the customer on the order to null
     * @param order the order to remove
     * @return the order that was removed
     */
    public BeerOrder removeOrder(BeerOrder order) {
        orders.remove(order);
        order.setCustomer(null);
        return order;
    }
}