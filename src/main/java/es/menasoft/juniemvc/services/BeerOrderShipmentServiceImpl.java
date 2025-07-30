package es.menasoft.juniemvc.services;

import es.menasoft.juniemvc.entities.BeerOrder;
import es.menasoft.juniemvc.entities.BeerOrderShipment;
import es.menasoft.juniemvc.exceptions.EntityNotFoundException;
import es.menasoft.juniemvc.mappers.BeerOrderShipmentMapper;
import es.menasoft.juniemvc.models.BeerOrderShipmentDto;
import es.menasoft.juniemvc.models.CreateBeerOrderShipmentCommand;
import es.menasoft.juniemvc.repositories.BeerOrderRepository;
import es.menasoft.juniemvc.repositories.BeerOrderShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class BeerOrderShipmentServiceImpl implements BeerOrderShipmentService {

    private final BeerOrderShipmentRepository beerOrderShipmentRepository;
    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderShipmentMapper beerOrderShipmentMapper;

    @Override
    @Transactional
    public BeerOrderShipmentDto createShipment(CreateBeerOrderShipmentCommand command) {
        BeerOrder beerOrder = beerOrderRepository.findById(command.beerOrderId())
                .orElseThrow(() -> new EntityNotFoundException("BeerOrder", command.beerOrderId()));

        BeerOrderShipment beerOrderShipment = beerOrderShipmentMapper.createBeerOrderShipmentCommandToBeerOrderShipment(command, beerOrder);
        beerOrder.addShipment(beerOrderShipment);

        BeerOrderShipment savedBeerOrderShipment = beerOrderShipmentRepository.save(beerOrderShipment);
        return beerOrderShipmentMapper.beerOrderShipmentToBeerOrderShipmentDto(savedBeerOrderShipment);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BeerOrderShipmentDto> getShipmentById(Integer id) {
        return beerOrderShipmentRepository.findById(id)
                .map(beerOrderShipmentMapper::beerOrderShipmentToBeerOrderShipmentDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeerOrderShipmentDto> getAllShipments() {
        return beerOrderShipmentRepository.findAll().stream()
                .map(beerOrderShipmentMapper::beerOrderShipmentToBeerOrderShipmentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeerOrderShipmentDto> getShipmentsByBeerOrderId(Integer beerOrderId) {
        return beerOrderShipmentRepository.findAllByBeerOrderId(beerOrderId).stream()
                .map(beerOrderShipmentMapper::beerOrderShipmentToBeerOrderShipmentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<BeerOrderShipmentDto> updateShipment(Integer id, BeerOrderShipmentDto shipmentDto) {
        return beerOrderShipmentRepository.findById(id)
                .map(existingShipment -> {
                    // Update basic properties
                    existingShipment.setShipmentDate(shipmentDto.shipmentDate());
                    existingShipment.setCarrier(shipmentDto.carrier());
                    existingShipment.setTrackingNumber(shipmentDto.trackingNumber());

                    // If beer order ID has changed, update the relationship
                    if (!existingShipment.getBeerOrder().getId().equals(shipmentDto.beerOrderId())) {
                        BeerOrder newBeerOrder = beerOrderRepository.findById(shipmentDto.beerOrderId())
                                .orElseThrow(() -> new EntityNotFoundException("BeerOrder", shipmentDto.beerOrderId()));
                        
                        // Remove from old beer order
                        existingShipment.getBeerOrder().removeShipment(existingShipment);
                        
                        // Add to new beer order
                        newBeerOrder.addShipment(existingShipment);
                    }

                    // Save the updated shipment
                    BeerOrderShipment savedShipment = beerOrderShipmentRepository.save(existingShipment);
                    return beerOrderShipmentMapper.beerOrderShipmentToBeerOrderShipmentDto(savedShipment);
                });
    }

    @Override
    @Transactional
    public boolean deleteShipment(Integer id) {
        return beerOrderShipmentRepository.findById(id)
                .map(shipment -> {
                    // Remove from beer order
                    shipment.getBeerOrder().removeShipment(shipment);
                    
                    // Delete the shipment
                    beerOrderShipmentRepository.delete(shipment);
                    return true;
                })
                .orElse(false);
    }
}