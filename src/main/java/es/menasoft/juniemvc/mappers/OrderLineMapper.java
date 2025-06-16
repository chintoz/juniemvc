package es.menasoft.juniemvc.mappers;

import es.menasoft.juniemvc.entities.Beer;
import es.menasoft.juniemvc.entities.BeerOrder;
import es.menasoft.juniemvc.entities.OrderLine;
import es.menasoft.juniemvc.models.OrderLineDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface OrderLineMapper {
    
    @Mappings({
        @Mapping(target = "beerId", source = "beer.id"),
        @Mapping(target = "beerName", source = "beer.beerName")
    })
    OrderLineDto orderLineToOrderLineDto(OrderLine orderLine);
    
    @Mappings({
        @Mapping(target = "beer", ignore = true),
        @Mapping(target = "beerOrder", ignore = true)
    })
    OrderLine orderLineDtoToOrderLine(OrderLineDto orderLineDto);
    
    default OrderLine orderLineDtoToOrderLine(OrderLineDto orderLineDto, Beer beer, BeerOrder beerOrder) {
        OrderLine orderLine = orderLineDtoToOrderLine(orderLineDto);
        orderLine.setBeer(beer);
        orderLine.setBeerOrder(beerOrder);
        return orderLine;
    }
}