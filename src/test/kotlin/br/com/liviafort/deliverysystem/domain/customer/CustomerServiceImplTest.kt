package br.com.liviafort.deliverysystem.domain.customer

import br.com.liviafort.deliverysystem.domain.exception.EntityAlreadyExistsException
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CustomerServiceImplTest {
    private lateinit var service: CustomerServiceImpl
    private lateinit var repository: CustomerRepository

    @BeforeEach
    fun setup() {
        repository = mockk(relaxed = false)
        service = CustomerServiceImpl(repository)
    }

    @Test
    fun `should save customer`() {
        // Given
        val customer = Customer(name = "livia", phone = "12345678", address = "Miguel Seabra, 324")

        justRun { repository.save(customer) }

        // When
        service.create(customer)

        // Then
        verify { repository.save(customer) }
    }

    @Test
    fun `should not save customer when there is a conflict`() {
        // Given
        val customer = Customer(name = "livia", phone = "12345678", address = "Miguel Seabra, 324")

        every { repository.save(customer) } throws EntityAlreadyExistsException("Customer already exists")

        // When
        assertThrows<EntityAlreadyExistsException> { service.create(customer) }
    }

}