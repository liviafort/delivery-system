package br.com.liviafort.deliverysystem.domain.order

interface OrderRepository {
    fun save(order: Order)
    fun findAll(): List<Order>
    fun remove(trackingCode: String)
}
