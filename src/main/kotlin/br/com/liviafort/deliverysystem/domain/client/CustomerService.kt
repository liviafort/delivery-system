package br.com.liviafort.deliverysystem.domain.client

interface CustomerService {
    fun create(customer: Customer)
    fun listing()
}
