package br.com.liviafort.deliverysystem.domain.order

import br.com.liviafort.deliverysystem.domain.exception.EntityAlreadyExistsException
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class OrderServiceImplTest{
    private lateinit var repository: OrderRepository
    private lateinit var service: OrderServiceImpl

    @BeforeEach
    fun setup() {
        repository = mockk(relaxed = false)
        service = OrderServiceImpl(repository)
    }

    @Test
    fun `should create order`() {
        // Given
        val order = Order(
            items = listOf(OrderItem(productId = "pizza quatro queijos", quantity = 2, price = 47.90)),
            trackingCode = "track123",
            totalPrice = 95.80,
        )

        justRun { repository.save(order) }

        // When
        service.create(order)

        // Then
        verify { repository.save(order) }
    }

    @Test
    fun `should not save order when there is a conflict`() {
        // Given
        val order = Order(
            items = listOf(OrderItem(productId = "pizza quatro queijos", quantity = 2, price = 47.90)),
            trackingCode = "track123",
            totalPrice = 95.80,
        )
        every { repository.save(order) } throws EntityAlreadyExistsException("Order already exists")

        // When
        assertThrows<EntityAlreadyExistsException> { service.create(order) }
    }

    @Test
    fun `should return all orders`() {
        // Given
        val orders = listOf(
            Order(
                items = listOf(OrderItem(productId = "pizza quatro queijos", quantity = 2, price = 47.90)),
                trackingCode = "track123",
                totalPrice = 95.80,
            ),
            Order(
                items = listOf(OrderItem(productId = "pizza quatro queijos", quantity = 2, price = 47.90)),
                trackingCode = "track34729",
                totalPrice = 95.80,
            ),
        )

        every { repository.findAll() } returns orders

        //When
        val result = service.listing()

        //Then
        verify { repository.findAll() }
        kotlin.test.assertEquals(orders, result)
    }

    @Test
    fun `should cancel order`() {
        // Given
        val order = Order(
            items = listOf(OrderItem(productId = "pizza quatro queijos", quantity = 2, price = 47.90)),
            trackingCode = "track123",
            totalPrice = 95.80,
        )

        justRun { repository.remove(order.trackingCode) }

        // When
        service.cancel(order.trackingCode)

        // Then
        verify { repository.remove(order.trackingCode) }
    }

}