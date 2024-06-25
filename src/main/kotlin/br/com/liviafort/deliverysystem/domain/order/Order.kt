package br.com.liviafort.deliverysystem.domain.order

import java.util.UUID

data class Order(
    val id: UUID = UUID.randomUUID(),
    val items: List<OrderItem>,
    val trackingCode: String,
    val totalPrice: Double,
){
    init {
        require(items.isNotEmpty()) { "Items must not be empty" }
        require(totalPrice >= 0) { "Total price must not be less than zero" }
    }
}

data class OrderItem(
    val productId: String,
    val quantity: Int,
    val price: Double
)