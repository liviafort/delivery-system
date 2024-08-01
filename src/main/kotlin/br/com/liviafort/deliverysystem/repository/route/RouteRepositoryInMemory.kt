package br.com.liviafort.deliverysystem.repository.route

import br.com.liviafort.deliverysystem.config.DatabaseConfig
import br.com.liviafort.deliverysystem.domain.deliveryman.DeliverymanRepository
import br.com.liviafort.deliverysystem.domain.order.OrderRepository
import br.com.liviafort.deliverysystem.domain.route.Route
import br.com.liviafort.deliverysystem.domain.route.RouteRepository
import br.com.liviafort.deliverysystem.domain.route.RouteStatus
import java.sql.SQLException
import java.util.NoSuchElementException
import java.util.UUID

class RouteRepositoryInMemory(
    private val deliverymanRepository: DeliverymanRepository,
    private val orderRepository: OrderRepository): RouteRepository {

    override fun save(route: Route) {
        val sql = "INSERT INTO route (id, destination, deliveryman_id, order_id, status, identifier) VALUES (?, ?, ?, ?, ?, ?)"
        val connection = DatabaseConfig.getConnection()
        try {
            connection.autoCommit = false // Para realizar transações

            val routeStatement = connection.prepareStatement(sql)
            routeStatement.setObject(1, route.id)
            routeStatement.setString(2, route.destination)
            routeStatement.setObject(3, route.deliveryman.id)
            routeStatement.setObject(4, route.order.id)
            routeStatement.setString(5, route.status.toString())
            routeStatement.setString(6, route.identifier)
            routeStatement.executeUpdate()

            connection.commit()
        } catch (e: SQLException) {
            connection.rollback()
            throw RuntimeException("Error saving route", e)
        } finally {
            connection.autoCommit = true
            connection.close()
        }
    }

    override fun findOne(routeId: UUID): Route {
        val sql = "SELECT * FROM route WHERE id = ?"
        val connection = DatabaseConfig.getConnection()
        try {
            val preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setObject(1, routeId)
            val resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) {
                return Route(
                    id = resultSet.getObject("id", UUID::class.java),
                    destination = resultSet.getString("destination"),
                    deliveryman = deliverymanRepository.findOne(resultSet.getObject("customer_id", UUID::class.java)),
                    order = orderRepository.findOne(resultSet.getObject("customer_id", UUID::class.java)),
                )
            } else {
                throw NoSuchElementException("Route with ID $routeId not found")
            }
        } catch (e: SQLException) {
            throw RuntimeException("Error retrieving route", e)
        } finally {
            connection.close()
        }
    }

    override fun findOneByTrackingCode(trackingCode: String): Route {
        val orderId = orderRepository.findOneByTrackingCode(trackingCode)
        val sql = "SELECT * FROM route WHERE order_id = ?"
        val connection = DatabaseConfig.getConnection()
        try {
            val preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setObject(1, orderId)
            val resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) {
                return Route(
                    id = resultSet.getObject("id", UUID::class.java),
                    destination = resultSet.getString("destination"),
                    deliveryman = deliverymanRepository.findOne(resultSet.getObject("customer_id", UUID::class.java)),
                    order = orderRepository.findOne(resultSet.getObject("customer_id", UUID::class.java)),
                )
            } else {
                throw NoSuchElementException("Route with Tracking Code $trackingCode not found")
            }
        } catch (e: SQLException) {
            throw RuntimeException("Error retrieving route", e)
        } finally {
            connection.close()
        }
    }

    override fun updateStatus(routeId: UUID, status: RouteStatus) {
        val sql = "UPDATE route SET status = ? WHERE id = ?"
        val connection = DatabaseConfig.getConnection()
        try {
            val preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setString(1, status.name)
            preparedStatement.setObject(2, routeId)
            val rowsAffected = preparedStatement.executeUpdate()

            if (rowsAffected == 0) {
                throw NoSuchElementException("Route with ID $routeId not found")
            }
        } catch (e: SQLException) {
            throw RuntimeException("Error updating route status", e)
        } finally {
            connection.close()
        }
    }

    override fun findAll(): List<Route> {
        val sql = "SELECT * FROM route"
        val connection = DatabaseConfig.getConnection()
        val routes = mutableListOf<Route>()
        try {
            val preparedStatement = connection.prepareStatement(sql)
            val resultSet = preparedStatement.executeQuery()
            while (resultSet.next()) {
                val routeId = resultSet.getObject("id", UUID::class.java)
                val destination = resultSet.getString("destination")
                val deliveryman = deliverymanRepository.findOne(resultSet.getObject("customer_id", UUID::class.java))
                val order = orderRepository.findOne(resultSet.getObject("customer_id", UUID::class.java))
                routes.add(
                    Route(
                    id = routeId,
                    destination = destination,
                    deliveryman = deliveryman,
                    order = order,
                )
                )
            }
        } catch (e: SQLException) {
            throw RuntimeException("Error retrieving all routes", e)
        } finally {
            connection.close()
        }
        return routes
    }
}