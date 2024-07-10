package br.com.liviafort.deliverysystem.repository.customer

import br.com.liviafort.deliverysystem.domain.customer.Customer
import br.com.liviafort.deliverysystem.domain.customer.CustomerRepository
import br.com.liviafort.deliverysystem.domain.exception.EntityAlreadyExistsException

class CustomerRepositoryInMemory : CustomerRepository {
    private val customers = mutableMapOf<String, Customer>()

    override fun save(customer: Customer) {
        if (customers.containsKey(customer.phone))
            throw EntityAlreadyExistsException("Customer phone number (${customer.phone} already exists")
        customers[customer.phone] = customer
    }

    override fun findAll(): List<Customer> {
        return customers.values.toList()
    }
}