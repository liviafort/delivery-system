package br.com.liviafort.deliverysystem.domain.customer


class CustomerServiceImpl(private val repository: CustomerRepository) : CustomerService {

    override fun create(customer: Customer) {
        repository.save(customer)
    }

    override fun listing(): List<Customer> {
        return repository.findAll()
    }
}
