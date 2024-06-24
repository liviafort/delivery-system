package br.com.liviafort.deliverysystem.domain.restaurant

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

class RestaurantServiceImplTest {
    private lateinit var service: RestaurantServiceImpl
    private lateinit var repository: RestaurantRepository

    @BeforeEach
    fun setup() {
        repository = mockk(relaxed = false)
        service = RestaurantServiceImpl(repository)
    }

    @Test
    fun `should save a restaurant`() {
        //Given
        val restaurant = Restaurant(name = "Pizzaria Straj", address = "Rua Alemida Junior, 45", category = "Pizzaria", cnpj = "38293/0003-123")

        justRun { repository.save(restaurant) }

        //Then
        service.create(restaurant)

        //When
        verify { repository.save(restaurant) }
    }

    @Test
    fun `should not save restaurant when there is a conflict`() {
        // Given
        val restaurant = Restaurant(name = "Pizzaria Straj", address = "Rua Alemida Junior, 45", category = "Pizzaria", cnpj = "38293/0003-123")

        every { repository.save(restaurant) } throws EntityAlreadyExistsException("Restaurant already exists")

        // When
        assertThrows<EntityAlreadyExistsException> { service.create(restaurant) }
    }

    @Test
    fun `should list all restaurant`() {
        //Given
        val restaurant = listOf(
            Restaurant(name = "Pizzaria Straj", address = "Rua Alemida Junior, 45", category = "Pizzaria", cnpj = "38293/0003-123"),
            Restaurant(name = "Hamburgueria Aureau", address = "Rua Caimbra, 121", category = "Pizzaria", cnpj = "33493/086786-123")
        )

        every { repository.findAll() } returns restaurant

        //When
        val result = service.listing()

        //Then
        verify { repository.findAll() }
        assertEquals(restaurant, result)
    }

    @Test
    fun `should return empty list when no restaurant exist`() {
        //Given
        every { repository.findAll() } returns emptyList()

        //When
        val result = service.listing()

        //Then
        verify { repository.findAll() }
        assertTrue { result.isEmpty() }
    }


}