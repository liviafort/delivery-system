package br.com.liviafort.deliverysystem.domain.route

interface RouteService {
    fun create(route: Route)
    fun changeStatus(route: Route, status: RouteStatus)
    fun listing(): List<Route>
}