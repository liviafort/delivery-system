package br.com.liviafort.deliverysystem.repository.deliveryman

import br.com.liviafort.deliverysystem.domain.deliveryman.Deliveryman
import br.com.liviafort.deliverysystem.domain.exception.EntityAlreadyExistsException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeliverymanRepositoryInMemoryIntegratedTest {
    private val repository = DeliverymanRepositoryInMemory()

    @Test
    fun `should persist a deliveryman`(){
        //Given
        val deliveryman = Deliveryman(name = "Josue", phone = "123212", vehicle = "Motocicleta")

        //When
        repository.save(deliveryman)

        //Then
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
    fun `should fail when a deliveryman already exist`() {
        // Given
        val deliveryman = Deliveryman(name = "Josue", phone = "123212", vehicle = "Motocicleta")
        val deliveryman2 = Deliveryman(name = "Josue", phone = "123212", vehicle = "Motocicleta")

        // When
        repository.save(deliveryman)
        assertThrows<EntityAlreadyExistsException> { repository.save(deliveryman2) }
    }

    @Test
    fun `should return all deliverymen`() {
        // Given
        val deliveryman = Deliveryman(name = "Josue", phone = "123212", vehicle = "Motocicleta")
        val deliveryman2 = Deliveryman(name = "Josue", phone = "68799", vehicle = "Motocicleta")

        repository.save(deliveryman)
        repository.save(deliveryman2)

        // When
        val deliverymen = repository.findAll()

        // Then
        assertEquals(2, deliverymen.size)
        assertTrue(deliverymen.containsAll(listOf(deliveryman, deliveryman2)))
    }

}
