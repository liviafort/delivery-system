package br.com.liviafort.deliverysystem.repository.route

import br.com.liviafort.deliverysystem.config.DatabaseConfig
import br.com.liviafort.deliverysystem.di.DependencyContainer
import br.com.liviafort.deliverysystem.domain.customer.Customer
import br.com.liviafort.deliverysystem.domain.deliveryman.Deliveryman
import br.com.liviafort.deliverysystem.domain.order.Order
import br.com.liviafort.deliverysystem.domain.order.OrderItem
import br.com.liviafort.deliverysystem.domain.restaurant.Restaurant
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantItem
import br.com.liviafort.deliverysystem.domain.route.Route
import br.com.liviafort.deliverysystem.domain.route.RouteStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.assertEquals

class RouteRepositoryInMemoryTest {
    private val repository = DependencyContainer.routeRepository
    private val deliverymanRepository = DependencyContainer.deliverymanRepository
    private val orderRepository = DependencyContainer.orderRepository
    private val restaurantRepository = DependencyContainer.restaurantRepository
    private val customerRepository = DependencyContainer.customerRepository

    @BeforeEach
    fun setup() {
        clearDatabase()
    }

    @AfterTest
    fun final() {
        clearDatabase()
    }

    @Test
    fun `should persist a route`() {
        // Given
        val restaurantItem = RestaurantItem(id = UUID.randomUUID(), name = "Pizza Quatro Queijos", price = 49.60)
        val restaurant = Restaurant(
            id = UUID.randomUUID(),
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "1237712",
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
            restaurant = restaurant,
        )
        orderRepository.save(order)

        val deliverymanId = UUID.randomUUID()
        val deliveryman = Deliveryman(id = deliverymanId, name = "John", phone = "987654", vehicle = "Bike")
        deliverymanRepository.save(deliveryman)

        val route = Route(
            id = UUID.randomUUID(),
            destination = "Rua das Flores, 123",
            deliveryman = deliveryman,
            order = order,
        )

        // When
        repository.save(route)

        // Then
        val routes = repository.findAll()
        assertEquals(1, routes.size)
        routes[0].also {
            assertEquals(route.id, it.id)
            assertEquals(route.destination, it.destination)
            assertEquals(route.deliveryman.id, it.deliveryman.id)
            assertEquals(route.order.id, it.order.id)
        }
    }

    @Test
    fun `should fail when a route already exists`() {
        // Given
        val restaurantItem = RestaurantItem(id = UUID.randomUUID(), name = "Pizza Quatro Queijos", price = 49.60)
        val restaurant = Restaurant(
            id = UUID.randomUUID(),
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "123215",
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
            restaurant = restaurant,
        )
        orderRepository.save(order)

        val deliveryman = Deliveryman(id = UUID.randomUUID(), name = "John", phone = "987654", vehicle = "Bike")
        deliverymanRepository.save(deliveryman)

        val route = Route(
            id = UUID.randomUUID(),
            destination = "Rua das Flores, 123",
            deliveryman = deliveryman,
            order = order,
        )

        // When
        repository.save(route)
        assertThrows<RuntimeException> { repository.save(route) }
    }

    @Test
    fun `should return all routes`() {
        // Given
        val restaurantItem1 = RestaurantItem(id = UUID.randomUUID(), name = "Pizza Quatro Queijos", price = 49.60)
        val restaurant1 = Restaurant(
            id = UUID.randomUUID(),
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "18883212",
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
            restaurant = restaurant1,
        )
        orderRepository.save(order1)

        val order2 = Order(
            id = UUID.randomUUID(),
            items = listOf(OrderItem(restaurantItem2, quantity = 2)),
            customer = customer2,
            restaurant = restaurant2,
        )
        orderRepository.save(order2)

        val deliveryman1 = Deliveryman(id = UUID.randomUUID(), name = "John", phone = "987654", vehicle = "Bike")
        deliverymanRepository.save(deliveryman1)

        val deliveryman2 = Deliveryman(id = UUID.randomUUID(), name = "Doe", phone = "123789", vehicle = "Car")
        deliverymanRepository.save(deliveryman2)

        val route1 = Route(
            id = UUID.randomUUID(),
            destination = "Rua das Flores, 123",
            deliveryman = deliveryman1,
            order = order1,
        )

        val route2 = Route(
            id = UUID.randomUUID(),
            destination = "Rua dos Pássaros, 456",
            deliveryman = deliveryman2,
            order = order2,
        )
        println(route1)
        println(route2)
        repository.save(route1)
        repository.save(route2)

        // When
        val routes = repository.findAll()
        println(routes)
        // Then
        assertTrue(routes.containsAll(listOf(route1, route2)))
    }

    @Test
    fun `should update route status`() {
        // Given
        val restaurantItem = RestaurantItem(id = UUID.randomUUID(), name = "Pizza Quatro Queijos", price = 49.60)
        val restaurant = Restaurant(
            id = UUID.randomUUID(),
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "123210",
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
            restaurant = restaurant,
        )
        orderRepository.save(order)

        val deliveryman = Deliveryman(id = UUID.randomUUID(), name = "John", phone = "987654", vehicle = "Bike")
        deliverymanRepository.save(deliveryman)

        val route = Route(
            id = UUID.randomUUID(),
            destination = "Rua das Flores, 123",
            deliveryman = deliveryman,
            order = order,
        )
        repository.save(route)

        // When
        repository.updateStatus(route.id, RouteStatus.FINISHED)
        // Then
        val updatedRoute = repository.findOne(route.id)
        assertEquals(RouteStatus.FINISHED, updatedRoute.status)
    }

    @Test
    fun `should find a specific route`() {
        // Given
        val restaurantItem = RestaurantItem(id = UUID.randomUUID(), name = "Pizza Quatro Queijos", price = 49.60)
        val restaurant = Restaurant(
            id = UUID.randomUUID(),
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "125212",
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
            restaurant = restaurant,
        )
        orderRepository.save(order)

        val deliveryman = Deliveryman(id = UUID.randomUUID(), name = "John", phone = "987654", vehicle = "Bike")
        deliverymanRepository.save(deliveryman)

        val route = Route(
            id = UUID.randomUUID(),
            destination = "Rua das Flores, 123",
            deliveryman = deliveryman,
            order = order,
        )
        repository.save(route)

        // When
        val result = repository.findOne(route.id)

        // Then
        assertEquals(route.id, result.id)
        assertEquals(route.destination, result.destination)
        assertEquals(route.deliveryman.id, result.deliveryman.id)
        assertEquals(route.order.id, result.order.id)
    }

    @Test
    fun `should find a specific route by tracking code`() {
        // Given
        val restaurantItem = RestaurantItem(id = UUID.randomUUID(), name = "Pizza Quatro Queijos", price = 49.60)
        val restaurant = Restaurant(
            id = UUID.randomUUID(),
            name = "Pizzaria Arnalds",
            address = "Rua Mania, 34",
            cnpj = "1266212",
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
            restaurant = restaurant,
        )
        orderRepository.save(order)

        val deliveryman = Deliveryman(id = UUID.randomUUID(), name = "John", phone = "987654", vehicle = "Bike")
        deliverymanRepository.save(deliveryman)

        val route = Route(
            id = UUID.randomUUID(),
            destination = "Rua das Flores, 123",
            deliveryman = deliveryman,
            order = order,
        )
        repository.save(route)

        // When
        val result = repository.findOneByTrackingCode(order.trackingCode)

        // Then
        assertEquals(route.id, result.id)
        assertEquals(route.destination, result.destination)
        assertEquals(route.deliveryman.id, result.deliveryman.id)
        assertEquals(route.order.id, result.order.id)
    }

    private fun clearDatabase() {
        val connection = DatabaseConfig.getConnection()
        connection.use { connection ->
            val statement = connection.createStatement()
            statement.executeUpdate("DELETE FROM route")
            statement.executeUpdate("DELETE FROM order_item")
            statement.executeUpdate("DELETE FROM orders")
            statement.executeUpdate("DELETE FROM restaurant_item")
            statement.executeUpdate("DELETE FROM restaurant")
            statement.executeUpdate("DELETE FROM customer")
            statement.executeUpdate("DELETE FROM deliveryman")
        }
    }
}
