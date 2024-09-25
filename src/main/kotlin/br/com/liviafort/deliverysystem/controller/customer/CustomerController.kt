package br.com.liviafort.deliverysystem.controller.customer

import br.com.liviafort.deliverysystem.domain.customer.Customer
import br.com.liviafort.deliverysystem.domain.customer.CustomerServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/customers")
class CustomerController(
    private val customerService: CustomerServiceImpl
) {

    @GetMapping
    fun listClients(): ResponseEntity<List<Customer>> {
        val customers = customerService.listing()
        return ResponseEntity.ok(customers)
    }

    @PostMapping
    fun registerNewClient(@RequestBody customer: Customer): ResponseEntity<Customer> {
        customerService.create(customer)
        return ResponseEntity.status(HttpStatus.CREATED).body(customer)
    }
}
