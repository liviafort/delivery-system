package br.com.liviafort.deliverysystem.repository.route

import br.com.liviafort.deliverysystem.domain.customer.Customer
import br.com.liviafort.deliverysystem.domain.deliveryman.Deliveryman
import br.com.liviafort.deliverysystem.domain.exception.EntityAlreadyExistsException
import br.com.liviafort.deliverysystem.domain.order.Order
import br.com.liviafort.deliverysystem.domain.order.OrderItem
import br.com.liviafort.deliverysystem.domain.restaurant.Restaurant
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantItem
import br.com.liviafort.deliverysystem.domain.route.Route
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class RouteRepositoryInMemoryTest {
    private val repository = RouteRepositoryInMemory()

    @Test
    fun `should persist a route`() {
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

        //When
        repository.save(route)

        //Then
        val routes = repository.findAll()
        assertEquals(1, routes.size)
        routes[0].also {
            assertEquals(route.id, it.id)
            assertEquals(route.identifier, it.identifier)
            assertEquals(route.status, it.status)
            assertEquals(route.destination, it.destination)
            assertEquals(route.deliveryman, it.deliveryman)
            assertEquals(route.order, it.order)
        }
    }

    @Test
    fun `should fail when a route already exist`() {
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


        // When
        repository.save(route)
        assertThrows<EntityAlreadyExistsException> { repository.save(route) }
    }

    @Test
    fun `should return all routes`() {
        // Given
        val order1 = Order(items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)), customer = Customer(name = "Ze", phone = "123456", address = "São João, 45"), restaurant = Restaurant(
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "123212",
            category = "Pizzaria",
            items = mutableSetOf(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60))
        ),)
        val deliveryman1 = Deliveryman(name = "Josue", phone = "123212", vehicle = "Motocicleta")
        val route1 = Route(destination = "Rua Amélia de Sá", deliveryman = deliveryman1, order = order1)

        val order2 = Order(items = listOf(OrderItem(RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60), quantity = 2)), customer = Customer(name = "Janilde", phone = "12345678", address = "São João, 45"), restaurant = Restaurant(
            name = "Hamburgueria Aureau",
            address = "Rua Caimbra, 121",
            category = "Hamburgueria",
            cnpj = "33493/086786-123",
            items = mutableSetOf(RestaurantItem(name = "Double cheese", price = 49.60))
        ),)
        val deliveryman2 = Deliveryman(name = "Rivaldo", phone = "238423243", vehicle = "Motocicleta")
        val route2 = Route(destination = "Av. Diamante de Cajá", deliveryman = deliveryman2, order = order2)

        repository.save(route1)
        repository.save(route2)

        // When
        val routes = repository.findAll()

        // Then
        assertEquals(2, routes.size)
        assertTrue(routes.containsAll(listOf(route1, route2)))
    }

    @Test
    fun `should find a specific route`() {
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

        repository.save(route)

        //When
        val result = repository.findOne(route.id)

        //Then
        assertEquals(route, result)
    }

    @Test
    fun `should find a specific route by tracking code`() {
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

        repository.save(route)

        //When
        val result = repository.findOneByTrackingCode(route.order.trackingCode)

        //Then
        assertEquals(route, result)
    }
}