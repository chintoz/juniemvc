package es.menasoft.juniemvc.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateBeerOrderCommand(
        @NotNull(message = "Customer ID is required")
        Integer customerId,
        
        @NotEmpty(message = "Order must have at least one order line")
        @Valid
        List<OrderLineDto> orderLines
) {}