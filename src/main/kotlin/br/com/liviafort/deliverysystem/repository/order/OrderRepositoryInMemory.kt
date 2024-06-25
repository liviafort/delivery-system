package br.com.liviafort.deliverysystem.repository.order

import br.com.liviafort.deliverysystem.domain.exception.EntityAlreadyExistsException
import br.com.liviafort.deliverysystem.domain.order.Order
import br.com.liviafort.deliverysystem.domain.order.OrderRepository

class OrderRepositoryInMemory : OrderRepository {
    private val orders = mutableMapOf<String, Order>()

    override fun save(order: Order) {
        if (orders.containsKey(order.trackingCode))
            throw EntityAlreadyExistsException("Order (${order.trackingCode} already exists")
        orders[order.trackingCode] = order
    }

    override fun findAll(): List<Order> {
        return orders.values.toList()
    }

    override fun remove(trackingCode: String) {
        if(!orders.containsKey(trackingCode))
            throw EntityAlreadyExistsException("Order ($trackingCode) don't exist")
        orders.remove(trackingCode)
    }
}