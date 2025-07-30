package es.menasoft.juniemvc.mappers;

import es.menasoft.juniemvc.entities.BeerOrder;
import es.menasoft.juniemvc.entities.BeerOrderShipment;
import es.menasoft.juniemvc.models.BeerOrderShipmentDto;
import es.menasoft.juniemvc.models.CreateBeerOrderShipmentCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface BeerOrderShipmentMapper {
    
    @Mappings({
        @Mapping(target = "beerOrderId", source = "beerOrder.id")
    })
    BeerOrderShipmentDto beerOrderShipmentToBeerOrderShipmentDto(BeerOrderShipment beerOrderShipment);
    
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "version", ignore = true),
        @Mapping(target = "createdDate", ignore = true),
        @Mapping(target = "updateDate", ignore = true),
        @Mapping(target = "beerOrder", ignore = true)
    })
    BeerOrderShipment beerOrderShipmentDtoToBeerOrderShipment(BeerOrderShipmentDto beerOrderShipmentDto);
    
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "version", ignore = true),
        @Mapping(target = "createdDate", ignore = true),
        @Mapping(target = "updateDate", ignore = true),
        @Mapping(target = "beerOrder", ignore = true)
    })
    BeerOrderShipment createBeerOrderShipmentCommandToBeerOrderShipment(CreateBeerOrderShipmentCommand command);
    
    default BeerOrderShipment beerOrderShipmentDtoToBeerOrderShipment(BeerOrderShipmentDto beerOrderShipmentDto, BeerOrder beerOrder) {
        BeerOrderShipment beerOrderShipment = beerOrderShipmentDtoToBeerOrderShipment(beerOrderShipmentDto);
        beerOrderShipment.setBeerOrder(beerOrder);
        return beerOrderShipment;
    }
    
    default BeerOrderShipment createBeerOrderShipmentCommandToBeerOrderShipment(CreateBeerOrderShipmentCommand command, BeerOrder beerOrder) {
        BeerOrderShipment beerOrderShipment = createBeerOrderShipmentCommandToBeerOrderShipment(command);
        beerOrderShipment.setBeerOrder(beerOrder);
        return beerOrderShipment;
    }
}