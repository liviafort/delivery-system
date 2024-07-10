package br.com.liviafort.deliverysystem.repository.route

import br.com.liviafort.deliverysystem.domain.exception.EntityAlreadyExistsException
import br.com.liviafort.deliverysystem.domain.exception.EntityNotFoundException
import br.com.liviafort.deliverysystem.domain.route.Route
import br.com.liviafort.deliverysystem.domain.route.RouteRepository
import br.com.liviafort.deliverysystem.domain.route.RouteStatus
import java.util.UUID

class RouteRepositoryInMemory: RouteRepository {
    private val routes = mutableMapOf<String, Route>()

    override fun save(route: Route) {
        if(routes.containsKey(route.identifier)){
            throw EntityAlreadyExistsException("Route number (${route.identifier} already exists")
        }
        routes[route.identifier] = route
    }

    override fun findOne(routeId: UUID): Route {
        return routes.values.firstOrNull { it.id == routeId }
            ?: throw EntityNotFoundException("Route ($routeId) not found")
    }

    override fun findOneByTrackingCode(trackingCode: String): Route {
        return routes.values.first { it.order.trackingCode == trackingCode }
    }

    override fun updateStatus(routeId: UUID, status: RouteStatus) {
        val route = findOne(routeId)
        route.status = status
    }

    override fun findAll(): List<Route> {
        return routes.values.toList()
    }
}