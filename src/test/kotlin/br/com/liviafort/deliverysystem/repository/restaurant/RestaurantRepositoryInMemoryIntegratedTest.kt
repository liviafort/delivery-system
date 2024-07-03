package br.com.liviafort.deliverysystem.repository.restaurant

import br.com.liviafort.deliverysystem.domain.exception.EntityAlreadyExistsException
import br.com.liviafort.deliverysystem.domain.restaurant.Restaurant
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantItem
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RestaurantRepositoryInMemoryIntegratedTest {
    private val repository = RestaurantRepositoryInMemory()

    @Test
    fun `should persist a restaurant`(){
        //Given
        val items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        val restaurant = Restaurant(name = "Pizzaria Arnalds", address = "Rua Mania, 34", cnpj = "123212", category = "Pizzaria", items = items)

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
        val items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        val restaurant = Restaurant(name = "Pizzaria Arnalds", address = "Rua Mania, 34", cnpj = "123212", category = "Pizzaria", items = items)

        // When
        repository.save(restaurant)
        assertThrows<EntityAlreadyExistsException> { repository.save(restaurant) }
    }

    @Test
    fun `should return all customers`() {
        // Given
        val items1 = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        val items2 = mutableSetOf(RestaurantItem(name = "Double cheese", price = 49.60))
        val restaurant1 = Restaurant(name = "Pizzaria Arnalds", address = "Rua Mania, 34", cnpj = "123212", category = "Pizzaria", items = items1)
        val restaurant2 = Restaurant(name = "Hamburguer", address = "Rua Mania, 90", cnpj = "83429842", category = "Hamburgueria", items = items2)

        repository.save(restaurant1)
        repository.save(restaurant2)

        // When
        val restaurants = repository.findAll()

        // Then
        assertEquals(2, restaurants.size)
        assertTrue(restaurants.containsAll(listOf(restaurant1, restaurant2)))
    }

    @Test
    fun `should save a new restaurant item`(){
        //Given
        val items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        val restaurant = Restaurant(name = "Pizzaria Arnalds", address = "Rua Mania, 34", cnpj = "123212", category = "Pizzaria", items = items)

        repository.save(restaurant)

        //When
        val item = RestaurantItem(name = "Pizza Frango com Catupiry", price = 50.00)
        repository.insertItem(restaurant, item)

        //Then
        val restaurants = repository.findAll()
        assertEquals(1, restaurants.size)
        restaurants[0].items.contains(item)
    }

}