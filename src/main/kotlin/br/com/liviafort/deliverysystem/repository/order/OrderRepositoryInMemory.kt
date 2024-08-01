package br.com.liviafort.deliverysystem.repository.order

import br.com.liviafort.deliverysystem.config.DatabaseConfig
import br.com.liviafort.deliverysystem.domain.customer.CustomerRepository
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
        val itemSql = "INSERT INTO order_items (id, order_id, restaurant_item_id, quantity) VALUES (?, ?, ?, ?)"
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
                itemStatement.setObject(3, item.restaurantItem.productId)
                itemStatement.setInt(4, item.quantity)
                itemStatement.addBatch()
            }
            itemStatement.executeBatch()

            connection.commit()
        } catch (e: SQLException) {
            connection.rollback()
            throw RuntimeException("Error saving order", e)
        } finally {
            connection.autoCommit = true // Restaura o comportamento padrão de auto-commit
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
        val sql = "SELECT id, restaurant_id, customer_id FROM orders WHERE trackingCode = ?"
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
                    customer = customer
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
        val sql = "DELETE FROM orders WHERE trackingCode = ?"
        val connection = DatabaseConfig.getConnection()
        try {
            val preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setObject(1, trackingCode)
            val rowsAffected = preparedStatement.executeUpdate()
            if (rowsAffected == 0) {
                throw NoSuchElementException("Order with tracking code $trackingCode not found")
            }
        } catch (e: SQLException) {
            throw RuntimeException("Error deleting order", e)
        } finally {
            connection.close()
        }
    }

    private fun getOrderItems(orderId: UUID): List<OrderItem> {
        val sql = "SELECT * FROM order_items WHERE order_id = ?"
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
        val sql = "SELECT * FROM order_items WHERE order_id = ?"
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