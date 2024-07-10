package br.com.liviafort.deliverysystem.domain.route

import java.util.UUID

interface RouteRepository {
    fun save(route: Route)
    fun findOne(routeId: UUID): Route
    fun findOneByTrackingCode(trackingCode: String): Route
    fun updateStatus(routeId: UUID, status: RouteStatus)
    fun findAll(): List<Route>
}