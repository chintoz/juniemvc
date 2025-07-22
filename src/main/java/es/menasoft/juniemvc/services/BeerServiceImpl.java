package es.menasoft.juniemvc.services;

import es.menasoft.juniemvc.entities.Beer;
import es.menasoft.juniemvc.mappers.BeerMapper;
import es.menasoft.juniemvc.models.BeerDto;
import es.menasoft.juniemvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    @Transactional
    public BeerDto saveBeer(BeerDto beerDto) {
        Beer beer = beerMapper.beerDtoToBeer(beerDto);
        Beer savedBeer = beerRepository.save(beer);
        return beerMapper.beerToBeerDto(savedBeer);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BeerDto> getBeerById(Integer id) {
        return beerRepository.findById(id)
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeerDto> getAllBeers() {
        return beerRepository.findAll().stream()
                .map(beerMapper::beerToBeerDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<BeerDto> getBeers(String beerName, Pageable pageable) {
        Page<Beer> beerPage;
        
        if (StringUtils.hasText(beerName)) {
            // If beerName is provided, search by name
            beerPage = beerRepository.findByBeerNameContainingIgnoreCase(beerName, pageable);
        } else {
            // If beerName is null or empty, return all beers with pagination
            beerPage = beerRepository.findAll(pageable);
        }
        
        return beerPage.map(beerMapper::beerToBeerDto);
    }

    @Override
    @Transactional
    public Optional<BeerDto> updateBeer(Integer id, BeerDto beerDto) {
        return beerRepository.findById(id)
                .map(existingBeer -> {
                    Beer beerToUpdate = beerMapper.beerDtoToBeer(beerDto);
                    existingBeer.setBeerName(beerToUpdate.getBeerName());
                    existingBeer.setBeerStyle(beerToUpdate.getBeerStyle());
                    existingBeer.setUpc(beerToUpdate.getUpc());
                    existingBeer.setPrice(beerToUpdate.getPrice());
                    existingBeer.setQuantityOnHand(beerToUpdate.getQuantityOnHand());
                    Beer savedBeer = beerRepository.save(existingBeer);
                    return beerMapper.beerToBeerDto(savedBeer);
                });
    }

    @Override
    @Transactional
    public boolean deleteBeer(Integer id) {
        return beerRepository.findById(id)
                .map(beer -> {
                    beerRepository.delete(beer);
                    return true;
                })
                .orElse(false);
    }
}
