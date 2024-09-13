package br.com.liviafort.deliverysystem.repository.deliveryman

import br.com.liviafort.deliverysystem.config.DatabaseConfig
import br.com.liviafort.deliverysystem.domain.deliveryman.Deliveryman
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class DeliverymanRepositoryInMemoryIntegratedTest {
    private val repository = DeliverymanRepositoryInMemory()

    @BeforeEach
    fun setup() {
        clearDatabase()
    }

    @Test
    fun `should persist a deliveryman`() {
        // Given
        val deliveryman = Deliveryman(id = UUID.randomUUID(), name = "Josue", phone = "1237812", vehicle = "Motocicleta")

        // When
        repository.save(deliveryman)

        // Then
        val deliverymen = repository.findAll()
        assertEquals(1, deliverymen.size)
        deliverymen[0].also {
            assertEquals(deliveryman.id, it.id)
            assertEquals(deliveryman.name, it.name)
            assertEquals(deliveryman.phone, it.phone)
            assertEquals(deliveryman.vehicle, it.vehicle)
        }
    }

    @Test
    fun `should fail when a deliveryman already exists`() {
        // Given
        val deliveryman = Deliveryman(id = UUID.randomUUID(), name = "Josue", phone = "123212", vehicle = "Motocicleta")
        val deliveryman2 = Deliveryman(id = UUID.randomUUID(), name = "Josue", phone = "123212", vehicle = "Motocicleta")

        // When
        repository.save(deliveryman)
        val exception = assertThrows<RuntimeException> {
            repository.save(deliveryman2)
        }

        // Then
        assertTrue(exception.message!!.contains("Error saving deliveryman"))
    }

    @Test
    fun `should return all deliverymen`() {
        // Given
        val deliveryman = Deliveryman(id = UUID.randomUUID(), name = "Josue", phone = "1299212", vehicle = "Motocicleta")
        val deliveryman2 = Deliveryman(id = UUID.randomUUID(), name = "Josue", phone = "6879799", vehicle = "Motocicleta")

        repository.save(deliveryman)
        repository.save(deliveryman2)

        // When
        val deliverymen = repository.findAll()

        // Then
        assertEquals(2, deliverymen.size)
        assertTrue(deliverymen.containsAll(listOf(deliveryman, deliveryman2)))
    }

    @Test
    fun `should find a specific deliveryman`() {
        // Given
        val deliveryman = Deliveryman(id = UUID.randomUUID(), name = "Josue", phone = "68799", vehicle = "Motocicleta")

        repository.save(deliveryman)

        // When
        val result = repository.findOne(deliveryman.id)

        // Then
        assertEquals(deliveryman, result)
    }

    private fun clearDatabase() {
        val connection = DatabaseConfig.getConnection()
        try {
            val statement = connection.createStatement()
            statement.executeUpdate("DELETE FROM deliveryman")
        } finally {
            connection.close()
        }
    }
}
