package es.menasoft.juniemvc.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record BeerOrderDto(
        //Read-only
        Integer id,
        Integer version,
        // Order status, values such as NEWS, PROCESSING, COMPLETED ..
        String orderStatus,

        // Read-only create date
        LocalDateTime createdDate,
        // Read-only update date
        LocalDateTime updateDate,
        
        @NotNull(message = "Customer ID is required")
        Integer customerId,
        
        @NotEmpty(message = "Order must have at least one order line")
        @Valid
        List<OrderLineDto> orderLines
) {}