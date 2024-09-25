package br.com.liviafort.deliverysystem.repository.route

import br.com.liviafort.deliverysystem.domain.deliveryman.DeliverymanRepository
import br.com.liviafort.deliverysystem.domain.order.OrderRepository
import br.com.liviafort.deliverysystem.domain.route.Route
import br.com.liviafort.deliverysystem.domain.route.RouteRepository
import br.com.liviafort.deliverysystem.domain.route.RouteStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class RouteRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate,
    private val deliverymanRepository: DeliverymanRepository,
    private val orderRepository: OrderRepository
) : RouteRepository {

    private val routeRowMapper = RowMapper { rs, _ ->
        Route(
            id = rs.getObject("id", UUID::class.java),
            destination = rs.getString("destination"),
            deliveryman = deliverymanRepository.findOne(rs.getObject("deliveryman_id", UUID::class.java)),
            order = orderRepository.findOne(rs.getObject("order_id", UUID::class.java)),
            status = RouteStatus.valueOf(rs.getString("status"))
        )
    }

    override fun save(route: Route) {
        val sql = "INSERT INTO route (id, destination, deliveryman_id, order_id, status, identifier) VALUES (?, ?, ?, ?, ?, ?)"

        try {
            jdbcTemplate.update(
                sql, route.id, route.destination, route.deliveryman.id, route.order.id,
                route.status.toString(), route.identifier
            )
        } catch (e: Exception) {
            throw RuntimeException("Error saving route", e)
        }
    }

    override fun findOne(routeId: UUID): Route {
        val sql = "SELECT * FROM route WHERE id = ?"
        return jdbcTemplate.queryForObject(sql, routeRowMapper, routeId)
            ?: throw NoSuchElementException("Route with ID $routeId not found")
    }

    override fun findOneByTrackingCode(trackingCode: String): Route {
        val order = orderRepository.findOneByTrackingCode(trackingCode)
        val sql = "SELECT * FROM route WHERE order_id = ?"
        return jdbcTemplate.queryForObject(sql, routeRowMapper, order.id)
            ?: throw NoSuchElementException("Route with Tracking Code $trackingCode not found")
    }

    override fun updateStatus(routeId: UUID, status: RouteStatus) {
        val sql = "UPDATE route SET status = ? WHERE id = ?"

        val rowsAffected = jdbcTemplate.update(sql, status.toString(), routeId)
        if (rowsAffected == 0) {
            throw NoSuchElementException("Route with ID $routeId not found")
        }
    }

    override fun findAll(): List<Route> {
        val sql = "SELECT * FROM route"
        return jdbcTemplate.query(sql, routeRowMapper)
    }
}
