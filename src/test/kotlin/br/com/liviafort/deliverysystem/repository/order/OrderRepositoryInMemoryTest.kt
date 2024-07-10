package br.com.liviafort.deliverysystem.repository.order

import br.com.liviafort.deliverysystem.domain.customer.Customer
import br.com.liviafort.deliverysystem.domain.exception.EntityAlreadyExistsException
import br.com.liviafort.deliverysystem.domain.exception.EntityNotFoundException
import br.com.liviafort.deliverysystem.domain.order.Order
import br.com.liviafort.deliverysystem.domain.order.OrderItem
import br.com.liviafort.deliverysystem.domain.restaurant.Restaurant
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantItem
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class OrderRepositoryInMemoryTest {
    private val repository = OrderRepositoryInMemory()

    @Test
    fun `should persist a order`() {
        //Given
        val order = Order(items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)), customer = Customer(name = "Ze", phone = "123456", address = "São João, 45"), restaurant = Restaurant(
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "123212",
            category = "Pizzaria",
            items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        ),)

        //When
        repository.save(order)

        //Then
        val orders = repository.findAll()
        assertEquals(1, orders.size)
        orders[0].also {
            assertEquals(order.id, it.id)
            assertEquals(order.items, it.items)
            assertEquals(order.trackingCode, it.trackingCode)
            assertEquals(order.totalPrice, it.totalPrice)
        }
    }

    @Test
    fun `should fail when a order already exist`() {
        // Given
        val order = Order(items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)), customer = Customer(name = "Ze", phone = "123456", address = "São João, 45"), restaurant = Restaurant(
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "123212",
            category = "Pizzaria",
            items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        ),)

        // When
        repository.save(order)
        assertThrows<EntityAlreadyExistsException> { repository.save(order) }
    }

    @Test
    fun `should return all orders`() {
        // Given
        val order1 = Order(items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)), customer = Customer(name = "Ze", phone = "123456", address = "São João, 45"), restaurant = Restaurant(
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "123212",
            category = "Pizzaria",
            items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        ),)

        val order2 = Order(items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)), customer = Customer(name = "Janilde", phone = "12345678", address = "São João, 45"), restaurant = Restaurant(
                name = "Hamburgueria Aureau",
                address = "Rua Caimbra, 121",
                category = "Hamburgueria",
                cnpj = "33493/086786-123",
                items = mutableSetOf(RestaurantItem(name = "Double cheese", price = 49.60))
        ),)

        repository.save(order1)
        repository.save(order2)

        // When
        val orders = repository.findAll()

        // Then
        assertEquals(2, orders.size)
        assertTrue(orders.containsAll(listOf(order1, order2)))
    }

    @Test
    fun `should remove an order`() {
        // Given
        val order = Order(items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)), customer = Customer(name = "Ze", phone = "123456", address = "São João, 45"), restaurant = Restaurant(
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "123212",
            category = "Pizzaria",
            items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        ),)
        repository.save(order)

        // When
        repository.remove(order.trackingCode)

        // Then
        val orders = repository.findAll()
        assertEquals(0, orders.size)
    }

    @Test
    fun `should fail when removing non-existent order`() {
        assertThrows<EntityNotFoundException> { repository.remove("nonExistentTrackCode") }
    }

    @Test
    fun `should correctly calculate the total price`() {
        //Given
        val order = Order(items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)), customer = Customer(name = "Ze", phone = "123456", address = "São João, 45"), restaurant = Restaurant(
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "123212",
            category = "Pizzaria",
            items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        ),)
        repository.save(order)

        //Then
        val calculatePrice: Double = order.items.sumOf { it.quantity * it.restaurantItem.price }
        assertEquals(order.totalPrice, calculatePrice)
    }

    @Test
    fun `should have ten characters in the tracking code`() {
        //Given
        val order = Order(items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)), customer = Customer(name = "Ze", phone = "123456", address = "São João, 45"), restaurant = Restaurant(
                name = "Pizzaria Arnalds",
                address = "Rua Mania, 34",
                cnpj = "123212",
                category = "Pizzaria",
                items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
            ),)
        repository.save(order)

        //Then
        val trackingCodeSize: Int = order.trackingCode.length
        assertEquals(10, trackingCodeSize)
    }

    @Test
    fun `should find a specific order`() {
        //Given
        val order = Order(items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)), customer = Customer(name = "Ze", phone = "123456", address = "São João, 45"), restaurant = Restaurant(
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "123212",
            category = "Pizzaria",
            items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        ),)

        repository.save(order)

        //When
        val result = repository.findOne(order.id)

        //Then
        assertEquals(order, result)
    }



}