package br.com.liviafort.deliverysystem.domain.order

import java.util.UUID
import kotlin.random.Random

data class Order(
    val id: UUID = UUID.randomUUID(),
    val items: List<OrderItem>
){
    val trackingCode: String = List(10) { Random.nextInt(0, 10) }.joinToString("")
    val totalPrice = items.sumOf { it.quantity * it.price }
    init {
        require(items.isNotEmpty()) { "Items must not be empty" }
    }
}

data class OrderItem(
    val productId: String,
    val quantity: Int,
    val price: Double
)