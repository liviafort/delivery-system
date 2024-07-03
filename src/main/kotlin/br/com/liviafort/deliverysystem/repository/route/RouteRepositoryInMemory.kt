package br.com.liviafort.deliverysystem.repository.route

import br.com.liviafort.deliverysystem.domain.exception.EntityAlreadyExistsException
import br.com.liviafort.deliverysystem.domain.route.Route
import br.com.liviafort.deliverysystem.domain.route.RouteRepository
import br.com.liviafort.deliverysystem.domain.route.RouteStatus

class RouteRepositoryInMemory: RouteRepository {
    private val routes = mutableMapOf<String, Route>()

    override fun save(route: Route) {
        if(routes.containsKey(route.identifier)){
            throw EntityAlreadyExistsException("Route number (${route.identifier} already exists")
        }
        routes[route.identifier] = route
    }

    override fun updateStatus(route: Route, status: RouteStatus) {
        route.status = status
    }

    override fun findAll(): List<Route> {
        return routes.values.toList()
    }
}