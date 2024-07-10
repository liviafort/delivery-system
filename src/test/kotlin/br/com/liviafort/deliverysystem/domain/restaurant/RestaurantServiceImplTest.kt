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
        val items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        val restaurant = Restaurant(name = "Pizzaria Arnalds", address = "Rua Mania, 34", cnpj = "123212", category = "Pizzaria", items = items)

        justRun { repository.save(restaurant) }

        //Then
        service.create(restaurant)

        //When
        verify { repository.save(restaurant) }
    }

    @Test
    fun `should not save restaurant when there is a conflict`() {
        // Given
        val items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        val restaurant = Restaurant(name = "Pizzaria Arnalds", address = "Rua Mania, 34", cnpj = "123212", category = "Pizzaria", items = items)

        every { repository.save(restaurant) } throws EntityAlreadyExistsException("Restaurant already exists")

        // When
        assertThrows<EntityAlreadyExistsException> { service.create(restaurant) }
    }

    @Test
    fun `should list all restaurant`() {
        //Given
        val items1 = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        val items2 = mutableSetOf(RestaurantItem(name = "Double cheese", price = 49.60))
        val restaurant = listOf(
            Restaurant(name = "Pizzaria Straj", address = "Rua Alemida Junior, 45", category = "Pizzaria", cnpj = "38293/0003-123", items = items1),
            Restaurant(name = "Hamburgueria Aureau", address = "Rua Caimbra, 121", category = "Hamburgueria", cnpj = "33493/086786-123", items = items2)
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

    @Test
    fun `should add a new item`() {
        //Given
        val items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        val restaurant = Restaurant(name = "Pizzaria Arnalds", address = "Rua Mania, 34", cnpj = "123212", category = "Pizzaria", items = items)

        val newItem = RestaurantItem(name = "Pizza Diavola", price = 47.20)
        justRun { repository.insertItem(restaurant.id, newItem) }

        //Then
        service.addItem(restaurant.id, newItem)

        //When
        verify { repository.insertItem(restaurant.id, newItem) }
    }

    @Test
    fun `should get a restaurant`() {
        //Given
        val items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        val restaurant = Restaurant(name = "Pizzaria Arnalds", address = "Rua Mania, 34", cnpj = "123212", category = "Pizzaria", items = items)

        every { repository.findOne(restaurant.id) } returns restaurant

        //When
        val result = service.getRestaurant(restaurant.id)

        //Then
        verify { repository.findOne(restaurant.id) }
        assertEquals(restaurant, result)
    }

}