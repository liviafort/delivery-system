package br.com.liviafort.deliverysystem.repository.order

import br.com.liviafort.deliverysystem.config.DatabaseConfig
import br.com.liviafort.deliverysystem.di.DependencyContainer
import br.com.liviafort.deliverysystem.domain.customer.Customer
import br.com.liviafort.deliverysystem.domain.exception.EntityAlreadyExistsException
import br.com.liviafort.deliverysystem.domain.exception.EntityNotFoundException
import br.com.liviafort.deliverysystem.domain.order.Order
import br.com.liviafort.deliverysystem.domain.order.OrderItem
import br.com.liviafort.deliverysystem.domain.restaurant.Restaurant
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantItem
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertEquals

class OrderRepositoryInMemoryTest {
    private val repository = DependencyContainer.orderRepository
    private val restaurantRepository = DependencyContainer.restaurantRepository
    private val customerRepository = DependencyContainer.customerRepository

    @BeforeEach
    fun setup() {
        clearDatabase()
    }

    @Test
    fun `should persist an order`() {
        // Given
        val restaurantItem = RestaurantItem(id = UUID.randomUUID(), name = "Pizza Quatro Queijos", price = 49.60)
        val restaurant = Restaurant(
            id = UUID.randomUUID(),
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "123212",
            category = "Pizzaria",
            items = mutableSetOf(restaurantItem)
        )
        restaurantRepository.save(restaurant)

        val customer = Customer(id = UUID.randomUUID(), name = "Ze", phone = "123456", address = "São João, 45")
        customerRepository.save(customer)

        val orderItem = OrderItem(restaurantItem = restaurantItem, quantity = 2)
        val order = Order(
            id = UUID.randomUUID(),
            items = listOf(orderItem),
            customer = customer,
            restaurant = restaurant
        )

        // When
        repository.save(order)

        // Then
        val orders = repository.findAll()
        assertEquals(1, orders.size)
        orders[0].also {
            assertEquals(order.id, it.id)
            assertEquals(order.items.size, it.items.size)
            assertEquals(order.items[0].restaurantItem.id, it.items[0].restaurantItem.id)
            assertEquals(order.customer.id, it.customer.id)
            assertEquals(order.restaurant.id, it.restaurant.id)
        }
    }

    @Test
    fun `should fail when an order already exists`() {
        // Given
        val restaurantItem = RestaurantItem(id = UUID.randomUUID(), name = "Pizza Quatro Queijos", price = 49.60)
        val restaurant = Restaurant(
            id = UUID.randomUUID(),
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "123212",
            category = "Pizzaria",
            items = mutableSetOf(restaurantItem)
        )
        restaurantRepository.save(restaurant)

        val customer = Customer(id = UUID.randomUUID(), name = "Ze", phone = "123456", address = "São João, 45")
        customerRepository.save(customer)

        val orderItem = OrderItem(restaurantItem = restaurantItem, quantity = 2)
        val order = Order(
            id = UUID.randomUUID(),
            items = listOf(orderItem),
            customer = customer,
            restaurant = restaurant
        )

        // When
        repository.save(order)
        val exception = assertThrows<EntityAlreadyExistsException> { repository.save(order) }

        // Then
        assertTrue(exception.message!!.contains("Order with the same ID already exists"))
    }

    @Test
    fun `should return all orders`() {
        // Given
        val restaurantItem1 = RestaurantItem(id = UUID.randomUUID(), name = "Pizza Quatro Queijos", price = 49.60)
        val restaurant1 = Restaurant(
            id = UUID.randomUUID(),
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "123212",
            category = "Pizzaria",
            items = mutableSetOf(restaurantItem1)
        )
        restaurantRepository.save(restaurant1)

        val restaurantItem2 = RestaurantItem(id = UUID.randomUUID(), name = "Double cheese", price = 49.60)
        val restaurant2 = Restaurant(
            id = UUID.randomUUID(),
            name = "Hamburgueria Aureau",
            address = "Rua Caimbra, 121",
            category = "Hamburgueria",
            cnpj = "33493/086786-123",
            items = mutableSetOf(restaurantItem2)
        )
        restaurantRepository.save(restaurant2)

        val customer1 = Customer(id = UUID.randomUUID(), name = "Ze", phone = "123456", address = "São João, 45")
        customerRepository.save(customer1)

        val customer2 = Customer(id = UUID.randomUUID(), name = "Janilde", phone = "12345678", address = "São João, 45")
        customerRepository.save(customer2)

        val order1 = Order(
            id = UUID.randomUUID(),
            items = listOf(OrderItem(restaurantItem1, quantity = 2)),
            customer = customer1,
            restaurant = restaurant1
        )

        val order2 = Order(
            id = UUID.randomUUID(),
            items = listOf(OrderItem(restaurantItem2, quantity = 2)),
            customer = customer2,
            restaurant = restaurant2
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
        val restaurantItem = RestaurantItem(id = UUID.randomUUID(), name = "Pizza Quatro Queijos", price = 49.60)
        val restaurant = Restaurant(
            id = UUID.randomUUID(),
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "123212",
            category = "Pizzaria",
            items = mutableSetOf(restaurantItem)
        )
        restaurantRepository.save(restaurant)

        val customer = Customer(id = UUID.randomUUID(), name = "Ze", phone = "123456", address = "São João, 45")
        customerRepository.save(customer)

        val orderItem = OrderItem(restaurantItem = restaurantItem, quantity = 2)
        val order = Order(
            id = UUID.randomUUID(),
            items = listOf(orderItem),
            customer = customer,
            restaurant = restaurant
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
        val exception = assertThrows<EntityNotFoundException> { repository.remove("nonExistentTrackCode") }
        assertTrue(exception.message!!.contains("Order with tracking code nonExistentTrackCode not found"))
    }

    @Test
    fun `should correctly calculate the total price`() {
        // Given
        val restaurantItem = RestaurantItem(id = UUID.randomUUID(), name = "Pizza Quatro Queijos", price = 49.60)
        val restaurant = Restaurant(
            id = UUID.randomUUID(),
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "123212",
            category = "Pizzaria",
            items = mutableSetOf(restaurantItem)
        )
        restaurantRepository.save(restaurant)

        val customer = Customer(id = UUID.randomUUID(), name = "Ze", phone = "123456", address = "São João, 45")
        customerRepository.save(customer)

        val orderItem = OrderItem(restaurantItem = restaurantItem, quantity = 2)
        val order = Order(
            id = UUID.randomUUID(),
            items = listOf(orderItem),
            customer = customer,
            restaurant = restaurant
        )
        repository.save(order)

        // Then
        val calculatePrice: Double = order.items.sumOf { it.quantity * it.restaurantItem.price }
        assertEquals(order.totalPrice, calculatePrice)
    }

    @Test
    fun `should have ten characters in the tracking code`() {
        // Given
        val restaurantItem = RestaurantItem(id = UUID.randomUUID(), name = "Pizza Quatro Queijos", price = 49.60)
        val restaurant = Restaurant(
            id = UUID.randomUUID(),
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "123212",
            category = "Pizzaria",
            items = mutableSetOf(restaurantItem)
        )
        restaurantRepository.save(restaurant)

        val customer = Customer(id = UUID.randomUUID(), name = "Ze", phone = "123456", address = "São João, 45")
        customerRepository.save(customer)

        val orderItem = OrderItem(restaurantItem = restaurantItem, quantity = 2)
        val order = Order(
            id = UUID.randomUUID(),
            items = listOf(orderItem),
            customer = customer,
            restaurant = restaurant
        )
        repository.save(order)

        // Then
        val trackingCodeSize: Int = order.trackingCode.length
        assertEquals(10, trackingCodeSize)
    }

    @Test
    fun `should find a specific order`() {
        // Given
        val restaurantItemId = UUID.randomUUID()
        val restaurantId = UUID.randomUUID()
        val customerId = UUID.randomUUID()
        val orderId = UUID.randomUUID()

        val restaurantItem = RestaurantItem(id = restaurantItemId, name = "Pizza Quatro Queijos", price = 49.60)
        val restaurant = Restaurant(
            id = restaurantId,
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "123212",
            category = "Pizzaria",
            items = mutableSetOf(restaurantItem)
        )
        restaurantRepository.save(restaurant)

        val customer = Customer(id = customerId, name = "Ze", phone = "123456", address = "São João, 45")
        customerRepository.save(customer)

        val orderItem = OrderItem(restaurantItem = restaurantItem, quantity = 2)
        val order = Order(
            id = orderId,
            items = listOf(orderItem),
            customer = customer,
            restaurant = restaurant
        )

        repository.save(order)

        // When
        val result = repository.findOne(order.id)

        // Then
        assertEquals(order.id, result.id)
        assertEquals(order.customer.id, result.customer.id)
        assertEquals(order.restaurant.id, result.restaurant.id)
        assertEquals(order.items.size, result.items.size)
        assertTrue(result.items.any { it.restaurantItem.id == restaurantItemId && it.quantity == orderItem.quantity })
    }

    private fun clearDatabase() {
        val connection = DatabaseConfig.getConnection()
        try {
            val statement = connection.createStatement()
            statement.executeUpdate("DELETE FROM order_item")
            statement.executeUpdate("DELETE FROM orders")
            statement.executeUpdate("DELETE FROM restaurant_item")
            statement.executeUpdate("DELETE FROM restaurant")
            statement.executeUpdate("DELETE FROM customer")
        } finally {
            connection.close()
        }
    }
}
