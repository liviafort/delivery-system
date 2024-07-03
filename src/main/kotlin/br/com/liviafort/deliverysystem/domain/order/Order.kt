package br.com.liviafort.deliverysystem.domain.order

import br.com.liviafort.deliverysystem.domain.customer.Customer
import br.com.liviafort.deliverysystem.domain.restaurant.Restaurant
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantItem
import java.util.UUID
import kotlin.random.Random

data class Order(
    val id: UUID = UUID.randomUUID(),
    val items: List<OrderItem>,
    val restaurant: Restaurant,
    val customer: Customer
){
    val trackingCode: String = List(10) { Random.nextInt(0, 10) }.joinToString("")
    val totalPrice = items.sumOf { it.quantity * it.restaurantItem.price }
    init {
        require(items.isNotEmpty()) { "Items must not be empty" }
    }
}

data class OrderItem(
    val restaurantItem: RestaurantItem,
    val quantity: Int
)