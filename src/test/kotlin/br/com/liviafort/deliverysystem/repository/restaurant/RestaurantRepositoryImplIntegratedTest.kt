package br.com.liviafort.deliverysystem.repository.restaurant

import br.com.liviafort.deliverysystem.domain.restaurant.Restaurant
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantItem
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional
class RestaurantRepositoryImplIntegratedTest {

    @Autowired
    private lateinit var repository: RestaurantRepositoryImpl

    @Test
    fun `should persist a restaurant`() {
        // Given
        val items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        val restaurant = Restaurant(id = UUID.randomUUID(), name = "Pizzaria Arnalds", address = "Rua Mania, 34", cnpj = "123212", category = "Pizzaria", items = items)

        // When
        repository.save(restaurant)

        // Then
        val restaurants = repository.findAll()
        assertEquals(1, restaurants.size)
        restaurants[0].also {
            assertEquals(restaurant.id, it.id)
            assertEquals(restaurant.name, it.name)
            assertEquals(restaurant.address, it.address)
            assertEquals(restaurant.cnpj, it.cnpj)
            assertEquals(restaurant.category, it.category)
            assertEquals(restaurant.items.size, it.items.size)
        }
    }

    @Test
    fun `should fail when a restaurant already exists`() {
        // Given
        val items1 = mutableSetOf(RestaurantItem(id = UUID.randomUUID(), name = "Pizza Quatro Queijos", price = 49.60))
        val items2 = mutableSetOf(RestaurantItem(id = UUID.randomUUID(), name = "Double cheese", price = 49.60))
        val restaurant1 = Restaurant(id = UUID.randomUUID(), name = "Pizzaria Arnalds", address = "Rua Mania, 34", cnpj = "12353654", category = "Pizzaria", items = items1)
        val restaurant2 = Restaurant(id = UUID.randomUUID(), name = "Hamburguer", address = "Rua Mania, 90", cnpj = "12353654", category = "Hamburgueria", items = items2)

        // When
        repository.save(restaurant1)
        val exception = assertThrows<RuntimeException> { repository.save(restaurant2) }

        // Then
        assertTrue(exception.message!!.contains("CNPJ already exists"))
    }

    @Test
    fun `should return all restaurants`() {
        // Given
        val items1 = mutableSetOf(RestaurantItem(id = UUID.randomUUID(), name = "Pizza Quatro Queijos", price = 49.60))
        val items2 = mutableSetOf(RestaurantItem(id = UUID.randomUUID(), name = "Double cheese", price = 49.60))
        val restaurant1 = Restaurant(id = UUID.randomUUID(), name = "Pizzaria Arnalds", address = "Rua Mania, 34", cnpj = "1276512", category = "Pizzaria", items = items1)
        val restaurant2 = Restaurant(id = UUID.randomUUID(), name = "Hamburguer", address = "Rua Mania, 90", cnpj = "83429842", category = "Hamburgueria", items = items2)

        repository.save(restaurant1)
        repository.save(restaurant2)

        // When
        val restaurants = repository.findAll()

        // Then
        assertEquals(2, restaurants.size)
        val savedRestaurant1 = restaurants.find { it.id == restaurant1.id }
        val savedRestaurant2 = restaurants.find { it.id == restaurant2.id }
        assertNotNull(savedRestaurant1)
        assertNotNull(savedRestaurant2)
        assertEquals(restaurant1.name, savedRestaurant1?.name)
        assertEquals(restaurant1.address, savedRestaurant1?.address)
        assertEquals(restaurant1.category, savedRestaurant1?.category)
        assertEquals(restaurant1.cnpj, savedRestaurant1?.cnpj)
        assertEquals(restaurant1.items.size, savedRestaurant1?.items?.size)
        assertTrue(savedRestaurant1?.items?.containsAll(restaurant1.items) ?: false)

        assertEquals(restaurant2.name, savedRestaurant2?.name)
        assertEquals(restaurant2.address, savedRestaurant2?.address)
        assertEquals(restaurant2.category, savedRestaurant2?.category)
        assertEquals(restaurant2.cnpj, savedRestaurant2?.cnpj)
        assertEquals(restaurant2.items.size, savedRestaurant2?.items?.size)
        assertTrue(savedRestaurant2?.items?.containsAll(restaurant2.items) ?: false)
    }

    @Test
    fun `should save a new restaurant item`() {
        // Given
        val items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        val restaurant = Restaurant(id = UUID.randomUUID(), name = "Pizzaria Arnalds", address = "Rua Mania, 34", cnpj = "1289792", category = "Pizzaria", items = items)

        repository.save(restaurant)

        // When
        val item = RestaurantItem(id = UUID.randomUUID(), name = "Pizza Frango com Catupiry", price = 50.00)
        repository.insertItem(restaurant.id, item)

        // Then
        val restaurants = repository.findAll()
        assertTrue(restaurants[0].items.contains(item))
    }

    @Test
    fun `should find a specific restaurant`() {
        // Given
        val itemId = UUID.randomUUID()
        val restaurantId = UUID.randomUUID()
        val items = mutableSetOf(RestaurantItem(id = itemId, name = "Pizza Quatro Queijos", price = 49.60))
        val restaurant = Restaurant(id = restaurantId, name = "Pizzaria Arnalds", address = "Rua Mania, 34", cnpj = "12534512", category = "Pizzaria", items = items)

        repository.save(restaurant)

        // When
        val result = repository.findOne(restaurant.id)

        // Then
        assertEquals(restaurant.id, result.id)
        assertEquals(restaurant.name, result.name)
        assertEquals(restaurant.address, result.address)
        assertEquals(restaurant.cnpj, result.cnpj)
        assertEquals(restaurant.category, result.category)
        assertEquals(restaurant.items.size, result.items.size)
        assertTrue(result.items.any { it.id == itemId && it.name == "Pizza Quatro Queijos" && it.price == 49.60 })
    }
}
