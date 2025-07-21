
# JPA Entity Relationship Implementation with Lombok

Based on the Beer ERD, here are detailed instructions for implementing the relationships in JPA with Lombok. The implementation should follow a beer ordering system structure with the following entities and relationships.

## Entity Structure

### 1. Beer Entity (Already Implemented)
The Beer entity is already implemented with basic properties. We need to enhance it with relationships.

```java
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Beer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Version
    private Integer version;
    
    private String beerName;
    private String beerStyle;
    private String upc;
    private Integer quantityOnHand;
    private BigDecimal price;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;
    
    @UpdateTimestamp
    private LocalDateTime updateDate;
    
    // Add relationship fields below
    @OneToMany(mappedBy = "beer")
    private Set<OrderLine> orderLines = new HashSet<>();
}
```

### 2. Customer Entity

```java
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
    
    @OneToMany(mappedBy = "customer")
    private Set<BeerOrder> orders = new HashSet<>();
}
```

### 3. BeerOrder Entity

```java
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
    
    @OneToMany(mappedBy = "beerOrder", cascade = CascadeType.ALL)
    private Set<OrderLine> orderLines = new HashSet<>();
}
```

### 4. OrderLine Entity

```java
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
```

## Implementation Guidelines

1. **Use Lombok Annotations**:
    - `@Getter` and `@Setter` for automatic getters and setters
    - `@Builder` for the builder pattern
    - `@NoArgsConstructor` and `@AllArgsConstructor` for constructors
    - Consider using `@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)` and `@EqualsAndHashCode.Include` on ID fields to avoid circular references

2. **JPA Relationship Best Practices**:
    - Use `mappedBy` to indicate the owning side of bidirectional relationships
    - Initialize collections to empty collections rather than null
    - Use appropriate cascade types (e.g., `CascadeType.ALL` for parent-child relationships)
    - Consider using `@JoinColumn` to specify foreign key column names

3. **Bidirectional Relationship Helper Methods**:
   Add helper methods to manage bidirectional relationships properly:

   ```java
   // In BeerOrder class
   public void addOrderLine(OrderLine orderLine) {
       orderLines.add(orderLine);
       orderLine.setBeerOrder(this);
   }
   
   public void removeOrderLine(OrderLine orderLine) {
       orderLines.remove(orderLine);
       orderLine.setBeerOrder(null);
   }
   
   // In Customer class
   public void addOrder(BeerOrder order) {
       orders.add(order);
       order.setCustomer(this);
   }
   
   public void removeOrder(BeerOrder order) {
       orders.remove(order);
       order.setCustomer(null);
   }
   ```

4. **Fetch Types**:
    - Use `FetchType.LAZY` for `@OneToMany` and `@ManyToMany` relationships to avoid performance issues
    - Example: `@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)`

5. **Indexing**:
    - Add database indexes for foreign keys and frequently queried fields:
   ```java
   @ManyToOne
   @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "FK_beer_order_customer"))
   private Customer customer;
   ```

6. **Audit Fields**:
    - All entities should include audit fields (createdDate, updateDate) as shown in the Beer entity

7. **Optimistic Locking**:
    - Use `@Version` annotation for optimistic locking to prevent concurrent modifications

8. **Validation**:
    - Add Bean Validation annotations (e.g., `@NotNull`, `@Size`) to enforce data integrity

## Database Schema Considerations

- Use appropriate column types and constraints
- Consider adding indexes for performance optimization
- Use foreign key constraints to maintain referential integrity

## Testing

- Create unit tests for entity relationships
- Test bidirectional relationship management
- Verify cascade operations work as expected

By following these guidelines, you'll create a robust JPA entity model with proper relationships for the beer ordering system.


# Create all layers for CRUD operations for BeerOder

Create all the layers involved with the creation of BeerOrder, repositories, services, dtos, mappers, controllers and tests.