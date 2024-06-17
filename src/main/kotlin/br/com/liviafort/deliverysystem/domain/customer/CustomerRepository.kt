package br.com.liviafort.deliverysystem.domain.customer

interface CustomerRepository {
    fun save(customer: Customer)
    fun findAll(): List<Customer>
}
