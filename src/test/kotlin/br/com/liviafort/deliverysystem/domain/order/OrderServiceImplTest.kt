package br.com.liviafort.deliverysystem.domain.order

import br.com.liviafort.deliverysystem.domain.customer.Customer
import br.com.liviafort.deliverysystem.domain.exception.EntityAlreadyExistsException
import br.com.liviafort.deliverysystem.domain.restaurant.Restaurant
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantItem
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class OrderServiceImplTest {
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
        val mockCustomer = mockk<Customer>()
        val mockRestaurant = mockk<Restaurant>()
        val order = Order(
            items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)),
            customer = mockCustomer,
            restaurant = mockRestaurant,
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
        val mockCustomer = mockk<Customer>()
        val mockRestaurant = mockk<Restaurant>()
        val order = Order(
            items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)),
            customer = mockCustomer,
            restaurant = mockRestaurant,
        )

        every { repository.save(order) } throws EntityAlreadyExistsException("Order already exists")

        // When
        assertThrows<EntityAlreadyExistsException> { service.create(order) }
    }

    @Test
    fun `should return all orders`() {
        // Given
        val mockCustomer = mockk<Customer>()
        val mockRestaurant = mockk<Restaurant>()
        val orders = listOf(
            Order(
                items = listOf(OrderItem(RestaurantItem(name = "Macarrão manjericão", price = 49.60), quantity = 2)),
                customer = mockCustomer,
                restaurant = mockRestaurant,
            ),
            Order(
                items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)),
                customer = mockCustomer,
                restaurant = mockRestaurant,
            ),
        )

        every { repository.findAll() } returns orders

        //When
        val result = service.listing()

        //Then
        verify { repository.findAll() }
        assertEquals(orders, result)
    }

    @Test
    fun `should cancel order`() {
        // Given
        val mockCustomer = mockk<Customer>()
        val mockRestaurant = mockk<Restaurant>()
        val order = Order(
            items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)),
            customer = mockCustomer,
            restaurant = mockRestaurant,
        )

        justRun { repository.remove(order.trackingCode) }

        // When
        service.cancel(order.trackingCode)

        // Then
        verify { repository.remove(order.trackingCode) }
    }

}