package br.com.liviafort.deliverysystem.domain.route

import br.com.liviafort.deliverysystem.domain.customer.Customer
import br.com.liviafort.deliverysystem.domain.deliveryman.Deliveryman
import br.com.liviafort.deliverysystem.domain.exception.EntityAlreadyExistsException
import br.com.liviafort.deliverysystem.domain.order.Order
import br.com.liviafort.deliverysystem.domain.order.OrderItem
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

class RouteServiceImplTest {
    private lateinit var repository: RouteRepository
    private lateinit var service: RouteServiceImpl

    @BeforeEach
    fun setup() {
        repository = mockk(relaxed = false)
        service = RouteServiceImpl(repository)
    }

    @Test
    fun `should create route`() {
        // Given
        val order = Order(items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)), customer = Customer(name = "Ze", phone = "123456", address = "São João, 45"), restaurant = Restaurant(
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "123212",
            category = "Pizzaria",
            items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        ),)
        val deliveryman = Deliveryman(name = "Josue", phone = "123212", vehicle = "Motocicleta")
        val route = Route(destination = "Rua Amélia de Sá", deliveryman = deliveryman, order = order)

        justRun { repository.save(route) }

        // When
        service.create(route)

        // Then
        verify { repository.save(route) }
    }

    @Test
    fun `should not save route when there is a conflict`() {
        // Given
        val order = Order(items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)), customer = Customer(name = "Ze", phone = "123456", address = "São João, 45"), restaurant = Restaurant(
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "123212",
            category = "Pizzaria",
            items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        ),)
        val deliveryman = Deliveryman(name = "Josue", phone = "123212", vehicle = "Motocicleta")
        val route = Route(destination = "Rua Amélia de Sá", deliveryman = deliveryman, order = order)
        every { repository.save(route) } throws EntityAlreadyExistsException("Order already exists")

        // When
        assertThrows<EntityAlreadyExistsException> { service.create(route) }
    }

    @Test
    fun `should update route status`() {
        // Given
        val order = Order(items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)), customer = Customer(name = "Ze", phone = "123456", address = "São João, 45"), restaurant = Restaurant(
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "123212",
            category = "Pizzaria",
            items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        ),)
        val deliveryman = Deliveryman(name = "Josue", phone = "123212", vehicle = "Motocicleta")
        val route = Route(destination = "Rua Amélia de Sá", deliveryman = deliveryman, order = order)

        justRun { repository.updateStatus(route, RouteStatus.FINISHED) }

        // When
        service.changeStatus(route, RouteStatus.FINISHED)

        // Then
        verify { repository.updateStatus(route, RouteStatus.FINISHED) }
    }

    @Test
    fun `should return all orders`() {
        // Given
        val mockOrder = mockk<Order>()
        val mockDeliveryman = mockk<Deliveryman>()
        val routes = listOf(
            Route(destination = "Rua Amélia de Sá", deliveryman = mockDeliveryman, order = mockOrder),
            Route(destination = "Rua Aumento Ares", deliveryman = mockDeliveryman, order = mockOrder),
        )

        every { repository.findAll() } returns routes

        //When
        val result = service.listing()

        //Then
        verify { repository.findAll() }
        assertEquals(routes, result)
    }

}