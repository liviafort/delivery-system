package br.com.liviafort.deliverysystem.repository.order

import br.com.liviafort.deliverysystem.domain.exception.EntityAlreadyExistsException
import br.com.liviafort.deliverysystem.domain.exception.EntityNotFoundException
import br.com.liviafort.deliverysystem.domain.order.Order
import br.com.liviafort.deliverysystem.domain.order.OrderRepository
import java.util.*

class OrderRepositoryInMemory : OrderRepository {
    private val orders = mutableMapOf<String, Order>()

    override fun save(order: Order) {
        if (orders.containsKey(order.trackingCode))
            throw EntityAlreadyExistsException("Order (${order.trackingCode} already exists")
        orders[order.trackingCode] = order
    }

    override fun findOne(orderId: UUID): Order {
        return orders.values.first { it.id == orderId }
    }

    override fun findAll(): List<Order> {
        return orders.values.toList()
    }

    override fun remove(trackingCode: String) {
        if(!orders.containsKey(trackingCode))
            throw EntityNotFoundException("Order ($trackingCode) don't exist")
        orders.remove(trackingCode)
    }
}