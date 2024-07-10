package br.com.liviafort.deliverysystem.domain.deliveryman

import br.com.liviafort.deliverysystem.domain.exception.EntityAlreadyExistsException
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DeliverymanServiceImplTest {
    private lateinit var service: DeliverymanServiceImpl
    private lateinit var repository: DeliverymanRepository

    @BeforeEach
    fun setup() {
        repository = mockk(relaxed = false)
        service = DeliverymanServiceImpl(repository)
    }

    @Test
    fun `should save a deliveryman`() {
        //Given
        val deliveryman = Deliveryman(name = "Jeremias", phone = "829134342", vehicle = "Car")

        justRun { repository.save(deliveryman) }

        //When
        service.create(deliveryman)

        //Then
        verify { repository.save(deliveryman) }

    }

    @Test
    fun `should not save deliveryman when there is a conflict`() {
        // Given
        val deliveryman = Deliveryman(name = "Jeremias", phone = "829134342", vehicle = "Car")

        every { repository.save(deliveryman) } throws EntityAlreadyExistsException("Deliveryman already exists")

        // When
        assertThrows<EntityAlreadyExistsException> { service.create(deliveryman) }
    }

    @Test
    fun `should list all deliverymen`() {
        //Given
        val deliverymen = listOf(
            Deliveryman(name = "Jeremias", phone = "829134342", vehicle = "Car"),
            Deliveryman(name = "Rivaldo", phone = "238423243", vehicle = "Motocicleta")
        )

        every { repository.findAll() } returns deliverymen

        //When
        val result = service.listing()

        //Then
        verify { repository.findAll() }
        assertEquals(deliverymen, result)
    }

    @Test
    fun `should return empty list when no deliveryman exist`() {
        //Given
        every { repository.findAll() } returns emptyList()

        //When
        val result = service.listing()

        //Then
        verify { repository.findAll() }
        assertTrue { result.isEmpty() }
    }

    @Test
    fun `should get a deliveryman`() {
        //Given
        val deliveryman = Deliveryman(name = "Jeremias", phone = "829134342", vehicle = "Car")

        every { repository.findOne(deliveryman.id) } returns deliveryman

        //When
        val result = service.getDeliveryman(deliveryman.id)

        //Then
        verify { repository.findOne(deliveryman.id) }
        assertEquals(deliveryman, result)
    }

}