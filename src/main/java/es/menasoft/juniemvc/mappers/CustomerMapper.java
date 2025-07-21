package es.menasoft.juniemvc.mappers;

import es.menasoft.juniemvc.entities.Customer;
import es.menasoft.juniemvc.models.CustomerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDto customerToCustomerDto(Customer customer);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "orders", ignore = true)
    Customer customerDtoToCustomer(CustomerDto customerDto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "orders", ignore = true)
    void updateCustomerFromDto(CustomerDto customerDto, @MappingTarget Customer customer);
}