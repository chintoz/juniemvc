package es.menasoft.juniemvc.mappers;

import es.menasoft.juniemvc.entities.BeerOrder;
import es.menasoft.juniemvc.entities.Customer;
import es.menasoft.juniemvc.models.BeerOrderDto;
import es.menasoft.juniemvc.models.CreateBeerOrderCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {OrderLineMapper.class})
public interface BeerOrderMapper {
    
    @Mappings({
        @Mapping(target = "customerId", source = "customer.id")
    })
    BeerOrderDto beerOrderToBeerOrderDto(BeerOrder beerOrder);
    
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "version", ignore = true),
        @Mapping(target = "createdDate", ignore = true),
        @Mapping(target = "updateDate", ignore = true),
        @Mapping(target = "customer", ignore = true),
        @Mapping(target = "orderLines", ignore = true)
    })
    BeerOrder beerOrderDtoToBeerOrder(BeerOrderDto beerOrderDto);
    
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "version", ignore = true),
        @Mapping(target = "createdDate", ignore = true),
        @Mapping(target = "updateDate", ignore = true),
        @Mapping(target = "orderStatus", constant = "NEW"),
        @Mapping(target = "customer", ignore = true),
        @Mapping(target = "orderLines", ignore = true)
    })
    BeerOrder createBeerOrderCommandToBeerOrder(CreateBeerOrderCommand command);
    
    default BeerOrder beerOrderDtoToBeerOrder(BeerOrderDto beerOrderDto, Customer customer) {
        BeerOrder beerOrder = beerOrderDtoToBeerOrder(beerOrderDto);
        beerOrder.setCustomer(customer);
        return beerOrder;
    }
    
    default BeerOrder createBeerOrderCommandToBeerOrder(CreateBeerOrderCommand command, Customer customer) {
        BeerOrder beerOrder = createBeerOrderCommandToBeerOrder(command);
        beerOrder.setCustomer(customer);
        return beerOrder;
    }
}