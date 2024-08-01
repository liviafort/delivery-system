package br.com.liviafort.deliverysystem.repository.deliveryman

import br.com.liviafort.deliverysystem.domain.deliveryman.Deliveryman
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeliverymanRepositoryInMemoryIntegratedTest {
    private val repository = DeliverymanRepositoryInMemory()

    @Test
    fun `should persist a deliveryman`(){
        //Given
        val deliveryman = Deliveryman(name = "Josue", phone = "1237812", vehicle = "Motocicleta")

        //When
        repository.save(deliveryman)

        //Then
        val deliverymen = repository.findAll()
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
        val exception = assertThrows<RuntimeException> {
            repository.save(deliveryman2)
        }
        assertTrue(exception.message!!.contains("Error saving deliveryman"))
    }

    @Test
    fun `should return all deliverymen`() {
        // Given
        val deliveryman = Deliveryman(name = "Josue", phone = "1299212", vehicle = "Motocicleta")
        val deliveryman2 = Deliveryman(name = "Josue", phone = "6879799", vehicle = "Motocicleta")

        repository.save(deliveryman)
        repository.save(deliveryman2)

        // When
        val deliverymen = repository.findAll()

        // Then
        assertTrue(deliverymen.containsAll(listOf(deliveryman, deliveryman2)))
    }

    @Test
    fun `should find a specific deliveryman`() {
        //Given
        val deliveryman = Deliveryman(name = "Josue", phone = "68799", vehicle = "Motocicleta")

        repository.save(deliveryman)

        //When
        val result = repository.findOne(deliveryman.id)

        //Then
        kotlin.test.assertEquals(deliveryman, result)
    }

}
