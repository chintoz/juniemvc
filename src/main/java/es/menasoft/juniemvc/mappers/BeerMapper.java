package es.menasoft.juniemvc.mappers;

import es.menasoft.juniemvc.entities.Beer;
import es.menasoft.juniemvc.models.BeerDto;
import es.menasoft.juniemvc.models.BeerPatchDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface BeerMapper {
    BeerDto beerToBeerDto(Beer beer);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Beer beerDtoToBeer(BeerDto beerDto);
    
    /**
     * Updates a Beer entity with non-null values from BeerPatchDto.
     * Null values in the patch DTO are ignored and don't overwrite existing values.
     *
     * @param patchDto the DTO containing fields to update
     * @param beer the beer entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "orderLines", ignore = true)
    @Mapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
             target = "beerName", source = "beerName")
    @Mapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
             target = "beerStyle", source = "beerStyle")
    @Mapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
             target = "description", source = "description")
    @Mapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
             target = "upc", source = "upc")
    @Mapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
             target = "quantityOnHand", source = "quantityOnHand")
    @Mapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
             target = "price", source = "price")
    void updateBeerFromPatchDto(BeerPatchDto patchDto, @MappingTarget Beer beer);
}