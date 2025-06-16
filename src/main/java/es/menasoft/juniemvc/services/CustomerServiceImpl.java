package es.menasoft.juniemvc.services;

import es.menasoft.juniemvc.entities.Customer;
import es.menasoft.juniemvc.mappers.CustomerMapper;
import es.menasoft.juniemvc.models.CustomerDto;
import es.menasoft.juniemvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional
    public CustomerDto saveCustomer(CustomerDto customerDto) {
        Customer customer = customerMapper.customerDtoToCustomer(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.customerToCustomerDto(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerDto> getCustomerById(Integer id) {
        return customerRepository.findById(id)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<CustomerDto> updateCustomer(Integer id, CustomerDto customerDto) {
        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    Customer customerToUpdate = customerMapper.customerDtoToCustomer(customerDto);
                    existingCustomer.setName(customerToUpdate.getName());
                    existingCustomer.setEmail(customerToUpdate.getEmail());
                    existingCustomer.setPhone(customerToUpdate.getPhone());
                    Customer savedCustomer = customerRepository.save(existingCustomer);
                    return customerMapper.customerToCustomerDto(savedCustomer);
                });
    }

    @Override
    @Transactional
    public boolean deleteCustomer(Integer id) {
        return customerRepository.findById(id)
                .map(customer -> {
                    customerRepository.delete(customer);
                    return true;
                })
                .orElse(false);
    }
}
