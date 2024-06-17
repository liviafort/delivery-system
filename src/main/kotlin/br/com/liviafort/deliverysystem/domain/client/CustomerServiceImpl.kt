package br.com.liviafort.deliverysystem.domain.client

class CustomerServiceImpl : CustomerService {
    private val customers = mutableListOf<Customer>()

    override fun create(customer: Customer) {
        try{
            when {
                customers.any { it.phone == customer.phone } -> {
                    throw IllegalArgumentException("Phone already registered") }
                else -> customers.add(customer)
            }
        }catch (e: IllegalArgumentException){
            println(e.message)
        }
    }

    override fun listing() {
        customers.forEachIndexed { index, customer ->
            println("${index + 1} - ${customer.name}")
        }
    }
}
