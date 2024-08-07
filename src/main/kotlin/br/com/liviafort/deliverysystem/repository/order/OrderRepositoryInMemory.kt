package br.com.liviafort.deliverysystem.repository.order

import br.com.liviafort.deliverysystem.config.DatabaseConfig
import br.com.liviafort.deliverysystem.domain.customer.CustomerRepository
import br.com.liviafort.deliverysystem.domain.exception.EntityAlreadyExistsException
import br.com.liviafort.deliverysystem.domain.exception.EntityNotFoundException
import br.com.liviafort.deliverysystem.domain.order.Order
import br.com.liviafort.deliverysystem.domain.order.OrderItem
import br.com.liviafort.deliverysystem.domain.order.OrderRepository
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantRepository
import java.sql.SQLException
import java.util.*

class OrderRepositoryInMemory(
    private val restaurantRepository: RestaurantRepository,
    private val customerRepository: CustomerRepository) : OrderRepository {

    override fun save(order: Order) {
        val sql = "INSERT INTO orders (id, restaurant_id, customer_id, tracking_code, total_price) VALUES (?, ?, ?, ?, ?)"
        val itemSql = "INSERT INTO order_item (id, order_id, restaurant_item_id, quantity) VALUES (?, ?, ?, ?)"
        val connection = DatabaseConfig.getConnection()
        try {
            // Order
            connection.autoCommit = false // Para realizar transações

            val orderStatement = connection.prepareStatement(sql)
            orderStatement.setObject(1, order.id)
            orderStatement.setObject(2, order.restaurant.id)
            orderStatement.setObject(3, order.customer.id)
            orderStatement.setString(4, order.trackingCode)
            orderStatement.setDouble(5, order.totalPrice)
            orderStatement.executeUpdate()

            // Order items
            val itemStatement = connection.prepareStatement(itemSql)
            for (item in order.items) {
                itemStatement.setObject(1, UUID.randomUUID())
                itemStatement.setObject(2, order.id)
                itemStatement.setObject(3, item.restaurantItem.id)
                itemStatement.setInt(4, item.quantity)
                itemStatement.addBatch()
            }
            itemStatement.executeBatch()

            connection.commit()
        } catch (e: SQLException) {
            connection.rollback()
            if (e.message?.contains("duplicate key value violates unique constraint") == true) {
                throw EntityAlreadyExistsException("Order with the same ID already exists")
            } else {
                throw RuntimeException("Error saving order", e)
            }
        } finally {
            connection.autoCommit = true
            connection.close()
        }
    }


    override fun findOne(orderId: UUID): Order {
        val sql = "SELECT id, restaurant_id, customer_id FROM orders WHERE id = ?"
        val connection = DatabaseConfig.getConnection()
        try {
            val preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setObject(1, orderId)
            val resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) {
                return Order(
                    id = resultSet.getObject("id", UUID::class.java),
                    restaurant = restaurantRepository.findOne(resultSet.getObject("restaurant_id", UUID::class.java)),
                    customer = customerRepository.findOne(resultSet.getObject("customer_id", UUID::class.java)),
                    items = getOrderItems(orderId)
                )
            } else {
                throw NoSuchElementException("Order with ID $orderId not found")
            }
        } catch (e: SQLException) {
            throw RuntimeException("Error retrieving order", e)
        } finally {
            connection.close()
        }
    }

    override fun findOneByTrackingCode(trackingCode: String): Order {
        val sql = "SELECT id, restaurant_id, customer_id FROM orders WHERE tracking_code = ?"
        val connection = DatabaseConfig.getConnection()
        try {
            val preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setObject(1, trackingCode)
            val resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) {
                return Order(
                    id = resultSet.getObject("id", UUID::class.java),
                    restaurant = restaurantRepository.findOne(resultSet.getObject("restaurant_id", UUID::class.java)),
                    customer = customerRepository.findOne(resultSet.getObject("customer_id", UUID::class.java)),
                    items = getOrderItemsByTrackingCode(trackingCode)
                )
            } else {
                throw NoSuchElementException("Order with tracking code $trackingCode not found")
            }
        } catch (e: SQLException) {
            throw RuntimeException("Error retrieving order", e)
        } finally {
            connection.close()
        }
    }

    override fun findAll(): List<Order> {
        val sql = "SELECT id, restaurant_id, customer_id FROM orders"
        val connection = DatabaseConfig.getConnection()
        val orders = mutableListOf<Order>()
        try {
            val preparedStatement = connection.prepareStatement(sql)
            val resultSet = preparedStatement.executeQuery()
            while (resultSet.next()) {
                val orderId = resultSet.getObject("id", UUID::class.java)
                val restaurant = restaurantRepository.findOne(resultSet.getObject("restaurant_id", UUID::class.java))
                val customer = customerRepository.findOne(resultSet.getObject("customer_id", UUID::class.java))
                val items = getOrderItems(orderId)
                orders.add(Order(
                    id = orderId,
                    items = items,
                    restaurant = restaurant,
                    customer = customer,
                ))
            }
        } catch (e: SQLException) {
            throw RuntimeException("Error retrieving all orders", e)
        } finally {
            connection.close()
        }
        return orders
    }

    override fun remove(trackingCode: String) {
        val deleteItemsSql = "DELETE FROM order_item WHERE order_id = (SELECT id FROM orders WHERE tracking_code = ?)"
        val deleteOrderSql = "DELETE FROM orders WHERE tracking_code = ?"
        val connection = DatabaseConfig.getConnection()
        try {
            connection.autoCommit = false

            val deleteItemsStatement = connection.prepareStatement(deleteItemsSql)
            deleteItemsStatement.setString(1, trackingCode)
            deleteItemsStatement.executeUpdate()

            val deleteOrderStatement = connection.prepareStatement(deleteOrderSql)
            deleteOrderStatement.setString(1, trackingCode)
            val rowsAffected = deleteOrderStatement.executeUpdate()
            if (rowsAffected == 0) {
                throw EntityNotFoundException("Order with tracking code $trackingCode not found")
            }

            connection.commit()
        } catch (e: SQLException) {
            connection.rollback()
            throw RuntimeException("Error deleting order", e)
        } finally {
            connection.autoCommit = true
            connection.close()
        }
    }

    private fun getOrderItems(orderId: UUID): List<OrderItem> {
        val sql = "SELECT * FROM order_item WHERE order_id = ?"
        val connection = DatabaseConfig.getConnection()
        val items = mutableListOf<OrderItem>()
        try {
            val preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setObject(1, orderId)
            val resultSet = preparedStatement.executeQuery()
            while (resultSet.next()) {
                val restaurantItem = restaurantRepository.findOneItem(resultSet.getObject("restaurant_item_id", UUID::class.java))
                val quantity = resultSet.getInt("quantity")
                items.add(OrderItem(restaurantItem, quantity))
            }
        } catch (e: SQLException) {
            throw RuntimeException("Error retrieving order items", e)
        } finally {
            connection.close()
        }
        return items
    }

    private fun getOrderItemsByTrackingCode(trackingCode: String): List<OrderItem> {
        val sql = """
        SELECT oi.*
        FROM order_item oi
        INNER JOIN orders o ON oi.order_id = o.id
        WHERE o.tracking_code = ?
    """
        val connection = DatabaseConfig.getConnection()
        val items = mutableListOf<OrderItem>()
        try {
            val preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setObject(1, trackingCode)
            val resultSet = preparedStatement.executeQuery()
            while (resultSet.next()) {
                val restaurantItem = restaurantRepository.findOneItem(resultSet.getObject("restaurant_item_id", UUID::class.java))
                val quantity = resultSet.getInt("quantity")
                items.add(OrderItem(restaurantItem, quantity))
            }
        } catch (e: SQLException) {
            throw RuntimeException("Error retrieving order items", e)
        } finally {
            connection.close()
        }
        return items
    }

}