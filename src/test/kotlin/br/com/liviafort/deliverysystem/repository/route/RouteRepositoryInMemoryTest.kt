package br.com.liviafort.deliverysystem.repository.route

import br.com.liviafort.deliverysystem.domain.deliveryman.Deliveryman
import br.com.liviafort.deliverysystem.domain.exception.EntityAlreadyExistsException
import br.com.liviafort.deliverysystem.domain.order.Order
import br.com.liviafort.deliverysystem.domain.route.Route
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RouteRepositoryInMemoryTest {
    private val repository = RouteRepositoryInMemory()

    @Test
    fun `should persist a route`() {
        //Given
        val mockOrder = mockk<Order>()
        val mockDeliveryman = mockk<Deliveryman>()
        val route = Route(destination = "Rua Amélia de Sá", deliveryman = mockDeliveryman, order = mockOrder)

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
        val mockOrder = mockk<Order>()
        val mockDeliveryman = mockk<Deliveryman>()
        val route = Route(destination = "Rua Amélia de Sá", deliveryman = mockDeliveryman, order = mockOrder)


        // When
        repository.save(route)
        assertThrows<EntityAlreadyExistsException> { repository.save(route) }
    }

    @Test
    fun `should return all routes`() {
        // Given
        val mockOrder = mockk<Order>()
        val mockDeliveryman = mockk<Deliveryman>()
        val route1 = Route(destination = "Rua Amélia de Sá", deliveryman = mockDeliveryman, order = mockOrder)

        val route2 = Route(destination = "Av. Diamante de Cajá", deliveryman = mockDeliveryman, order = mockOrder)

        repository.save(route1)
        repository.save(route2)

        // When
        val routes = repository.findAll()

        // Then
        assertEquals(2, routes.size)
        assertTrue(routes.containsAll(listOf(route1, route2)))
    }
}