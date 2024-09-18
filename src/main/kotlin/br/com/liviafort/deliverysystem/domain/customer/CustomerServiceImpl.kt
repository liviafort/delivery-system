package br.com.liviafort.deliverysystem.domain.customer

import org.springframework.stereotype.Service

@Service
class CustomerServiceImpl(private val repository: CustomerRepository) : CustomerService {

    override fun create(customer: Customer) {
        repository.save(customer)
    }

    override fun listing(): List<Customer> {
        return repository.findAll()
    }
}
