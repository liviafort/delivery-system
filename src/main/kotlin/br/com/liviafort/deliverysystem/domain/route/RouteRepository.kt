package br.com.liviafort.deliverysystem.domain.route

interface RouteRepository {
    fun save(route: Route)
    fun updateStatus(route: Route, status: RouteStatus)
    fun findAll(): List<Route>
}