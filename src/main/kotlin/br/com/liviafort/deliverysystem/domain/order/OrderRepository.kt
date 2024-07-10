package br.com.liviafort.deliverysystem.domain.order

import java.util.UUID

interface OrderRepository {
    fun save(order: Order)
    fun findOne(orderId: UUID): Order
    fun findAll(): List<Order>
    fun remove(trackingCode: String)
}
