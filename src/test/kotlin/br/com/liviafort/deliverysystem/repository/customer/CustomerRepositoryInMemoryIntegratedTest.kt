package br.com.liviafort.deliverysystem.repository.customer

import br.com.liviafort.deliverysystem.domain.customer.Customer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CustomerRepositoryInMemoryIntegratedTest {
    private val repository = CustomerRepositoryInMemory()

    @Test
    fun `should persist a customer`() {
        // Given
        val customer = Customer(name = "Ze", phone = "8270392", address = "São João, 45")

        // When
        repository.save(customer)

        // Then
        val customers = repository.findAll()
        customers[0].also {
            assertEquals(customer.id, it.id)
            assertEquals(customer.name, it.name)
            assertEquals(customer.phone, it.phone)
            assertEquals(customer.address, it.address)
        }
    }

    @Test
    fun `should fail when a customer already exist`() {
        // Given
        val customer = Customer(name = "Ze", phone = "123456", address = "São João, 45")
        val customer2 = Customer(name = "Janilde", phone = "123456", address = "São João, 45")

        // When
        repository.save(customer)
        val exception = assertThrows<RuntimeException> {
            repository.save(customer2)
        }
        assertTrue(exception.message!!.contains("Error saving customer"))
    }

    @Test
    fun `should return all customers`() {
        // Given
        val customer = Customer(name = "Ze", phone = "123856", address = "São João, 45")
        val customer2 = Customer(name = "Janilde", phone = "12345678", address = "São João, 45")

        repository.save(customer)
        repository.save(customer2)

        // When
        val customers = repository.findAll()

        // Then
        assertTrue(customers.containsAll(listOf(customer, customer2)))
    }
}