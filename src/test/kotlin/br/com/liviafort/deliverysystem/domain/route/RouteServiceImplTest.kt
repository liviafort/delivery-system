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

        justRun { repository.updateStatus(route.id, RouteStatus.FINISHED) }

        // When
        service.changeStatus(route.id, RouteStatus.FINISHED)

        // Then
        verify { repository.updateStatus(route.id, RouteStatus.FINISHED) }
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
        val deliveryman1 = Deliveryman(name = "Josue", phone = "123212", vehicle = "Motocicleta")

        val order2 = Order(items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)), customer = Customer(name = "Janilde", phone = "12345678", address = "São João, 45"), restaurant = Restaurant(
            name = "Hamburgueria Aureau",
            address = "Rua Caimbra, 121",
            category = "Hamburgueria",
            cnpj = "33493/086786-123",
            items = mutableSetOf(RestaurantItem(name = "Double cheese", price = 49.60))
        ),)
        val deliveryman2 = Deliveryman(name = "Janildo", phone = "234234", vehicle = "Motocicleta")

        val routes = listOf(
            Route(destination = "Rua Amélia de Sá", deliveryman = deliveryman1, order = order1),
            Route(destination = "Rua Eumares Ares", deliveryman = deliveryman2, order = order2),
        )

        every { repository.findAll() } returns routes

        //When
        val result = service.listing()

        //Then
        verify { repository.findAll() }
        assertEquals(routes, result)
    }

    @Test
    fun `should get a route`() {
        //Given
        val order = Order(items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)), customer = Customer(name = "Ze", phone = "123456", address = "São João, 45"), restaurant = Restaurant(
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "123212",
            category = "Pizzaria",
            items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        ),)
        val deliveryman = Deliveryman(name = "Josue", phone = "123212", vehicle = "Motocicleta")
        val route = Route(destination = "Rua Amélia de Sá", deliveryman = deliveryman, order = order)

        every { repository.findOne(route.id) } returns route

        //When
        val result = service.getRoute(route.id)

        //Then
        verify { repository.findOne(route.id) }
        assertEquals(route, result)
    }

    @Test
    fun `should get a route by tracking code`() {
        //Given
        val order = Order(items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)), customer = Customer(name = "Ze", phone = "123456", address = "São João, 45"), restaurant = Restaurant(
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "123212",
            category = "Pizzaria",
            items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        ),)
        val deliveryman = Deliveryman(name = "Josue", phone = "123212", vehicle = "Motocicleta")
        val route = Route(destination = "Rua Amélia de Sá", deliveryman = deliveryman, order = order)

        every { repository.findOneByTrackingCode(route.order.trackingCode) } returns route

        //When
        val result = service.getRouteByTrackingCode(route.order.trackingCode)

        //Then
        verify { repository.findOneByTrackingCode(route.order.trackingCode) }
        assertEquals(route, result)
    }

}