package br.com.liviafort.deliverysystem.repository.customer

import br.com.liviafort.deliverysystem.domain.customer.Customer
import br.com.liviafort.deliverysystem.domain.exception.EntityAlreadyExistsException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CustomerRepositoryInMemoryIntegratedTest {
    private val repository = CustomerRepositoryInMemory()

    @Test
    fun `should persist a customer`() {
        // Given
        val customer = Customer(name = "Ze", phone = "123456", address = "São João, 45")

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
    fun `should fail when a customer already exist`() {
        // Given
        val customer = Customer(name = "Ze", phone = "123456", address = "São João, 45")
        val customer2 = Customer(name = "Janilde", phone = "123456", address = "São João, 45")

        // When
        repository.save(customer)
        assertThrows<EntityAlreadyExistsException> { repository.save(customer2) }
    }
}