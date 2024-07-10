package br.com.liviafort.deliverysystem.domain.order

import java.util.UUID

interface OrderService {
    fun create(order: Order)
    fun getOrder(orderId: UUID): Order
    fun listing(): List<Order>
    fun cancel(trackingCode: String)
}