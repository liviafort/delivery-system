package br.com.liviafort.deliverysystem.repository.customer

import br.com.liviafort.deliverysystem.domain.customer.Customer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional
class CustomerRepositoryImplIntegrationTest {

    @Autowired
    private lateinit var repository: CustomerRepositoryImpl

    @Test
    fun `should persist a customer`() {
        // Given
        val customer = Customer(id = UUID.randomUUID(), name = "Ze", phone = "8270392", address = "São João, 45")

        // When
        repository.save(customer)

        // Then
        val customers = repository.findAll()
        assertEquals(1, customers.size)
        customers[0].also {
            assertEquals(customer.id, it.id)
            assertEquals(customer.name, it.name)
            assertEquals(customer.phone, it.phone)
            assertEquals(customer.address, it.address)
        }
    }

    @Test
    fun `should fail when a customer already exists`() {
        // Given
        val customer = Customer(id = UUID.randomUUID(), name = "Ze", phone = "123456", address = "São João, 45")
        val customer2 = Customer(id = UUID.randomUUID(), name = "Janilde", phone = "123456", address = "São João, 45")

        // When
        repository.save(customer)
        val exception = assertThrows<RuntimeException> {
            repository.save(customer2)
        }

        // Then
        assertTrue(exception.message!!.contains("Error saving customer"))
    }

    @Test
    fun `should return all customers`() {
        // Given
        val customer = Customer(id = UUID.randomUUID(), name = "Ze", phone = "123856", address = "São João, 45")
        val customer2 = Customer(id = UUID.randomUUID(), name = "Janilde", phone = "12345678", address = "São João, 45")

        repository.save(customer)
        repository.save(customer2)

        // When
        val customers = repository.findAll()

        // Then
        assertEquals(2, customers.size)
        assertTrue(customers.containsAll(listOf(customer, customer2)))
    }
}
