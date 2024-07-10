package br.com.liviafort.deliverysystem.domain.route

import java.util.UUID

interface RouteService {
    fun create(route: Route)
    fun getRoute(routeId: UUID): Route
    fun getRouteByTrackingCode(trackingCode: String): Route
    fun changeStatus(routeId: UUID, status: RouteStatus)
    fun listing(): List<Route>
}