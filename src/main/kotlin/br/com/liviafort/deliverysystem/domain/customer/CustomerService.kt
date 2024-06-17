package br.com.liviafort.deliverysystem.domain.customer

interface CustomerService {
    fun create(customer: Customer)
    fun listing()
}
