package br.com.liviafort.deliverysystem.repository.restaurant

import br.com.liviafort.deliverysystem.domain.exception.EntityAlreadyExistsException
import br.com.liviafort.deliverysystem.domain.restaurant.Restaurant
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RestaurantRepositoryInMemoryIntegratedTest {
    private val repository = RestaurantRepositoryInMemory()

    @Test
    fun `should persist a restaurant`(){
        //Given
        val restaurant = Restaurant(name = "Pizzaria Arnalds", address = "Rua Mania, 34", cnpj = "123212", category = "Pizzaria")

        //When
        repository.save(restaurant)

        //Then
        val restaurants = repository.findAll()
        assertEquals(1, restaurants.size)
        restaurants[0].also {
            assertEquals(restaurant.id, it.id)
            assertEquals(restaurant.name, it.name)
            assertEquals(restaurant.address, it.address)
            assertEquals(restaurant.cnpj, it.cnpj)
            assertEquals(restaurant.category, it.category)
        }
    }

    @Test
    fun `should fail when a restaurant already exist`() {
        // Given
        val restaurant = Restaurant(name = "Pizzaria Arnalds", address = "Rua Mania, 34", cnpj = "123212", category = "Pizzaria")

        // When
        repository.save(restaurant)
        assertThrows<EntityAlreadyExistsException> { repository.save(restaurant) }
    }

    @Test
    fun `should return all customers`() {
        // Given
        val restaurant = Restaurant(name = "Pizzaria Arnalds", address = "Rua Mania, 34", cnpj = "123212", category = "Pizzaria")
        val restaurant2 = Restaurant(name = "Hamburguer", address = "Rua Mania, 90", cnpj = "83429842", category = "Hamburgueria")

        repository.save(restaurant)
        repository.save(restaurant2)

        // When
        val restaurants = repository.findAll()

        // Then
        assertEquals(2, restaurants.size)
        assertTrue(restaurants.containsAll(listOf(restaurant, restaurant2)))
    }

}