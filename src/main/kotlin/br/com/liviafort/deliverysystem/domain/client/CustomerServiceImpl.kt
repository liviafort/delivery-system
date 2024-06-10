package br.com.liviafort.deliverysystem.domain.client

class CustomerServiceImpl : CustomerService {
    private val customers = mutableListOf<Customer>()

    override fun create(customer: Customer) {
        when {
            customers.any { it.phone == customer.phone } -> {
                throw IllegalArgumentException("Phone already registered")
            }
            else -> customers.add(customer)
        }
    }
}
