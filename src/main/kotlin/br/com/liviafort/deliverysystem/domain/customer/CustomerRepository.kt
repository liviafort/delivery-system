package br.com.liviafort.deliverysystem.domain.customer

import java.util.UUID

interface CustomerRepository {
    fun save(customer: Customer)
    fun findAll(): List<Customer>
    fun findOne(customerId: UUID): Customer
}
