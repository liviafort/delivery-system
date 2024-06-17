package br.com.liviafort.deliverysystem.domain.customer

class CustomerServiceImpl(private val repository: CustomerRepository) : CustomerService {
    private val customers = mutableListOf<Customer>()

    override fun create(customer: Customer) {
        repository.save(customer)
    }

    override fun listing() {
        customers.forEachIndexed { index, customer ->
            println("${index + 1} - ${customer.name}")
        }
    }
}
