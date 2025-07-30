package es.menasoft.juniemvc.services;

import es.menasoft.juniemvc.entities.Beer;
import es.menasoft.juniemvc.entities.BeerOrder;
import es.menasoft.juniemvc.entities.Customer;
import es.menasoft.juniemvc.entities.OrderLine;
import es.menasoft.juniemvc.exceptions.EntityNotFoundException;
import es.menasoft.juniemvc.mappers.BeerOrderMapper;
import es.menasoft.juniemvc.mappers.OrderLineMapper;
import es.menasoft.juniemvc.models.BeerOrderDto;
import es.menasoft.juniemvc.models.CreateBeerOrderCommand;
import es.menasoft.juniemvc.models.OrderLineDto;
import es.menasoft.juniemvc.repositories.BeerOrderRepository;
import es.menasoft.juniemvc.repositories.BeerRepository;
import es.menasoft.juniemvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class BeerOrderServiceImpl implements BeerOrderService {

    private final BeerOrderRepository beerOrderRepository;
    private final CustomerRepository customerRepository;
    private final BeerRepository beerRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final OrderLineMapper orderLineMapper;

    @Override
    @Transactional
    public BeerOrderDto createBeerOrder(CreateBeerOrderCommand command) {
        Customer customer = customerRepository.findById(command.customerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer", command.customerId()));

        BeerOrder beerOrder = beerOrderMapper.createBeerOrderCommandToBeerOrder(command, customer);

        // Add order lines
        command.orderLines().forEach(orderLineDto -> {
            Beer beer = beerRepository.findById(orderLineDto.beerId())
                    .orElseThrow(() -> new EntityNotFoundException("Beer", orderLineDto.beerId()));

            OrderLine orderLine = orderLineMapper.orderLineDtoToOrderLine(orderLineDto, beer, beerOrder);
            beerOrder.addOrderLine(orderLine);
        });

        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);
        return beerOrderMapper.beerOrderToBeerOrderDto(savedBeerOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BeerOrderDto> getBeerOrderById(Integer id) {
        return beerOrderRepository.findById(id)
                .map(beerOrderMapper::beerOrderToBeerOrderDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeerOrderDto> getAllBeerOrders() {
        return beerOrderRepository.findAll().stream()
                .map(beerOrderMapper::beerOrderToBeerOrderDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeerOrderDto> getBeerOrdersByCustomerId(Integer customerId) {
        return beerOrderRepository.findAllByCustomerId(customerId).stream()
                .map(beerOrderMapper::beerOrderToBeerOrderDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<BeerOrderDto> updateBeerOrder(Integer id, BeerOrderDto beerOrderDto) {
        return beerOrderRepository.findById(id)
                .map(existingBeerOrder -> {
                    // Update basic properties
                    existingBeerOrder.setOrderStatus(beerOrderDto.orderStatus());

                    // Save the updated order
                    BeerOrder savedBeerOrder = beerOrderRepository.save(existingBeerOrder);
                    return beerOrderMapper.beerOrderToBeerOrderDto(savedBeerOrder);
                });
    }

    @Override
    @Transactional
    public Optional<BeerOrderDto> updateBeerOrderStatus(Integer id, String status) {
        return beerOrderRepository.findById(id)
                .map(existingBeerOrder -> {
                    existingBeerOrder.setOrderStatus(status);
                    BeerOrder savedBeerOrder = beerOrderRepository.save(existingBeerOrder);
                    return beerOrderMapper.beerOrderToBeerOrderDto(savedBeerOrder);
                });
    }

    @Override
    @Transactional
    public boolean deleteBeerOrder(Integer id) {
        return beerOrderRepository.findById(id)
                .map(beerOrder -> {
                    beerOrderRepository.delete(beerOrder);
                    return true;
                })
                .orElse(false);
    }
}
