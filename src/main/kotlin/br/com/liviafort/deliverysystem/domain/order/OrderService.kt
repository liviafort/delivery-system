package br.com.liviafort.deliverysystem.domain.order

interface OrderService {
    fun create(order: Order)
    fun listing(): List<Order>
    fun cancel(trackingCode: String)
}