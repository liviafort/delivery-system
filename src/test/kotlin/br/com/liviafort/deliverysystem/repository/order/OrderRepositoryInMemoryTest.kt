package br.com.liviafort.deliverysystem.repository.order

import br.com.liviafort.deliverysystem.domain.exception.EntityAlreadyExistsException
import br.com.liviafort.deliverysystem.domain.order.Order
import br.com.liviafort.deliverysystem.domain.order.OrderItem
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class OrderRepositoryInMemoryTest {
    private val repository = OrderRepositoryInMemory()

    @Test
    fun `should persist a order`(){
        //Given
        val order = Order(
            id = UUID.randomUUID(),
            items = listOf(OrderItem(productId = "hamburguer", quantity = 1, price = 47.90)),
            trackingCode = "239423",
            totalPrice = 95.80,
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
        val order = Order(
            id = UUID.randomUUID(),
            items = listOf(OrderItem(productId = "pizza quatro queijos", quantity = 2, price = 47.90)),
            trackingCode = "track123",
            totalPrice = 95.80,
        )

        // When
        repository.save(order)
        assertThrows<EntityAlreadyExistsException> { repository.save(order) }
    }

    @Test
    fun `should return all orders`() {
        // Given
        val order1 = Order(
            id = UUID.randomUUID(),
            items = listOf(OrderItem(productId = "pizza quatro queijos", quantity = 2, price = 47.90)),
            trackingCode = "track123",
            totalPrice = 95.80,
        )

        val order2 = Order(
            id = UUID.randomUUID(),
            items = listOf(OrderItem(productId = "lasagna", quantity = 1, price = 25.50)),
            trackingCode = "track456",
            totalPrice = 25.50,
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
        val order = Order(
            id = UUID.randomUUID(),
            items = listOf(OrderItem(productId = "pizza quatro queijos", quantity = 2, price = 47.90)),
            trackingCode = "83920343",
            totalPrice = 95.80,
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
        assertThrows<EntityAlreadyExistsException> { repository.remove("nonExistentTrackCode") }
    }

}