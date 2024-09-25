package br.com.liviafort.deliverysystem.repository.order

import br.com.liviafort.deliverysystem.domain.customer.CustomerRepository
import br.com.liviafort.deliverysystem.domain.exception.EntityAlreadyExistsException
import br.com.liviafort.deliverysystem.domain.exception.EntityNotFoundException
import br.com.liviafort.deliverysystem.domain.order.Order
import br.com.liviafort.deliverysystem.domain.order.OrderItem
import br.com.liviafort.deliverysystem.domain.order.OrderRepository
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class OrderRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate,
    private val restaurantRepository: RestaurantRepository,
    private val customerRepository: CustomerRepository
) : OrderRepository {

    private val orderRowMapper = RowMapper{ rs, _ ->
        val orderId = rs.getObject("id", UUID::class.java)
        Order(
            id = orderId,
            restaurant = restaurantRepository.findOne(rs.getObject("restaurant_id", UUID::class.java)),
            customer = customerRepository.findOne(rs.getObject("customer_id", UUID::class.java)),
            items = getOrderItems(orderId)
        )
    }

    private val orderItemRowMapper = RowMapper<OrderItem> { rs, _ ->
        OrderItem(
            restaurantItem = restaurantRepository.findOneItem(rs.getObject("restaurant_item_id", UUID::class.java)),
            quantity = rs.getInt("quantity")
        )
    }

    override fun save(order: Order) {
        val sql = "INSERT INTO orders (id, restaurant_id, customer_id, tracking_code, total_price) VALUES (?, ?, ?, ?, ?)"
        val itemSql = "INSERT INTO order_item (id, order_id, restaurant_item_id, quantity) VALUES (?, ?, ?, ?)"

        try {
            jdbcTemplate.update(sql, order.id, order.restaurant.id, order.customer.id, order.trackingCode, order.totalPrice)

            order.items.forEach { item ->
                jdbcTemplate.update(itemSql, UUID.randomUUID(), order.id, item.restaurantItem.id, item.quantity)
            }
        } catch (e: Exception) {
            if (e.message?.contains("duplicate key value violates unique constraint") == true) {
                throw EntityAlreadyExistsException("Order with the same ID already exists")
            } else {
                throw RuntimeException("Error saving order", e)
            }
        }
    }

    override fun findOne(orderId: UUID): Order {
        val sql = "SELECT id, restaurant_id, customer_id FROM orders WHERE id = ?"
        return jdbcTemplate.queryForObject(sql, orderRowMapper, orderId)
            ?: throw EntityNotFoundException("Order with ID $orderId not found")
    }

    override fun findOneByTrackingCode(trackingCode: String): Order {
        val sql = "SELECT id, restaurant_id, customer_id FROM orders WHERE tracking_code = ?"
        return jdbcTemplate.queryForObject(sql, orderRowMapper, trackingCode)
            ?: throw EntityNotFoundException("Order with tracking code $trackingCode not found")
    }

    override fun findAll(): List<Order> {
        val sql = "SELECT id, restaurant_id, customer_id FROM orders"
        return jdbcTemplate.query(sql, orderRowMapper)
    }

    override fun remove(trackingCode: String) {
        val deleteItemsSql = "DELETE FROM order_item WHERE order_id = (SELECT id FROM orders WHERE tracking_code = ?)"
        val deleteOrderSql = "DELETE FROM orders WHERE tracking_code = ?"

        try {
            jdbcTemplate.update(deleteItemsSql, trackingCode)
            val rowsAffected = jdbcTemplate.update(deleteOrderSql, trackingCode)
            if (rowsAffected == 0) {
                throw EntityNotFoundException("Order with tracking code $trackingCode not found")
            }
        } catch (e: Exception) {
            throw RuntimeException("Error deleting order", e)
        }
    }

    private fun getOrderItems(orderId: UUID): List<OrderItem> {
        val sql = "SELECT * FROM order_item WHERE order_id = ?"
        return jdbcTemplate.query(sql, orderItemRowMapper, orderId)
    }

    private fun getOrderItemsByTrackingCode(trackingCode: String): List<OrderItem> {
        val sql = """
        SELECT * 
        FROM order_item 
        WHERE order_id = (SELECT id FROM orders WHERE tracking_code = ?)
    """
        return jdbcTemplate.query(sql, orderItemRowMapper, trackingCode)
    }

}
