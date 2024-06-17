package br.com.liviafort.deliverysystem.repository.customer

import br.com.liviafort.deliverysystem.domain.customer.Customer
import br.com.liviafort.deliverysystem.domain.customer.CustomerRepository

class CustomerRepositoryInMemory : CustomerRepository {
    override fun save(customer: Customer) {
        TODO("Not yet implemented")
    }

    override fun findAll() {
        TODO("Not yet implemented")
    }
}