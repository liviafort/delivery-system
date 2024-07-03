package br.com.liviafort.deliverysystem.repository.order

import br.com.liviafort.deliverysystem.domain.customer.Customer
import br.com.liviafort.deliverysystem.domain.exception.EntityAlreadyExistsException
import br.com.liviafort.deliverysystem.domain.exception.EntityNotFoundException
import br.com.liviafort.deliverysystem.domain.order.Order
import br.com.liviafort.deliverysystem.domain.order.OrderItem
import br.com.liviafort.deliverysystem.domain.restaurant.Restaurant
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantItem
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class OrderRepositoryInMemoryTest {
    private val repository = OrderRepositoryInMemory()

    @Test
    fun `should persist a order`() {
        //Given
        val mockCustomer = mockk<Customer>()
        val mockRestaurant = mockk<Restaurant>()
        val order = Order(
            items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)),
            customer = mockCustomer,
            restaurant = mockRestaurant,
        )

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
        val mockCustomer = mockk<Customer>()
        val mockRestaurant = mockk<Restaurant>()
        val order = Order(
            items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)),
            customer = mockCustomer,
            restaurant = mockRestaurant,
        )

        // When
        repository.save(order)
        assertThrows<EntityAlreadyExistsException> { repository.save(order) }
    }

    @Test
    fun `should return all orders`() {
        // Given
        val mockCustomer = mockk<Customer>()
        val mockRestaurant = mockk<Restaurant>()
        val order1 = Order(
            items = listOf(OrderItem(RestaurantItem(name = "Macarrão manjericão", price = 49.60), quantity = 2)),
            customer = mockCustomer,
            restaurant = mockRestaurant,
        )

        val order2 = Order(
            items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)),
            customer = mockCustomer,
            restaurant = mockRestaurant,
        )

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
        val mockCustomer = mockk<Customer>()
        val mockRestaurant = mockk<Restaurant>()
        val order = Order(
            items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)),
            customer = mockCustomer,
            restaurant = mockRestaurant,
        )
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
        val mockCustomer = mockk<Customer>()
        val mockRestaurant = mockk<Restaurant>()
        val order = Order(
            items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)),
            customer = mockCustomer,
            restaurant = mockRestaurant,
        )
        repository.save(order)

        //Then
        val calculatePrice: Double = order.items.sumOf { it.quantity * it.restaurantItem.price }
        assertEquals(order.totalPrice, calculatePrice)
    }

    @Test
    fun `should have ten characters in the tracking code`() {
        //Given
        val mockCustomer = mockk<Customer>()
        val mockRestaurant = mockk<Restaurant>()
        val order = Order(
            items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)),
            customer = mockCustomer,
            restaurant = mockRestaurant,
        )
        repository.save(order)

        //Then
        val trackingCode: Int = order.trackingCode.length
        assertEquals(10, trackingCode)
    }



}