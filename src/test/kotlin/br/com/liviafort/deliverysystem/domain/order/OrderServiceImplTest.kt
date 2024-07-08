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
        val order = Order(items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)), customer = Customer(name = "Ze", phone = "123456", address = "São João, 45"), restaurant = Restaurant(
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "123212",
            category = "Pizzaria",
            items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        ),)

        justRun { repository.save(order) }

        // When
        service.create(order)

        // Then
        verify { repository.save(order) }
    }

    @Test
    fun `should not save order when there is a conflict`() {
        // Given
        val order = Order(items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)), customer = Customer(name = "Ze", phone = "123456", address = "São João, 45"), restaurant = Restaurant(
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "123212",
            category = "Pizzaria",
            items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        ),)

        every { repository.save(order) } throws EntityAlreadyExistsException("Order already exists")

        // When
        assertThrows<EntityAlreadyExistsException> { service.create(order) }
    }

    @Test
    fun `should return all orders`() {
        // Given
        val orders = listOf(
            Order(items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)), customer = Customer(name = "Ze", phone = "123456", address = "São João, 45"), restaurant = Restaurant(
                name = "Pizzaria Arnalds",
                address = "Rua Mania, 34",
                cnpj = "123212",
                category = "Pizzaria",
                items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
            ),),
            Order(items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)), customer = Customer(name = "Janilde", phone = "12345678", address = "São João, 45"), restaurant = Restaurant(
                name = "Hamburgueria Aureau",
                address = "Rua Caimbra, 121",
                category = "Hamburgueria",
                cnpj = "33493/086786-123",
                items = mutableSetOf(RestaurantItem(name = "Double cheese", price = 49.60))
            ),)
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
        val order = Order(items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)), customer = Customer(name = "Ze", phone = "123456", address = "São João, 45"), restaurant = Restaurant(
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "123212",
            category = "Pizzaria",
            items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        ),)

        justRun { repository.remove(order.trackingCode) }

        // When
        service.cancel(order.trackingCode)

        // Then
        verify { repository.remove(order.trackingCode) }
    }

}