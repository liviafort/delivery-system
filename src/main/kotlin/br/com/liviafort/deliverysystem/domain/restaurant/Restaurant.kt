package br.com.liviafort.deliverysystem.domain.restaurant

import java.util.*

data class Restaurant(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val address: String,
    val category: String,
    val cnpj: String,
    val items: MutableSet<RestaurantItem>
) {
    init {
        require(name.isNotBlank()) { "Name must not be blank" }
        require(address.isNotBlank()) { "Address must not be blank" }
        require(category.isNotBlank()) { "Category must not be blank" }
        require(cnpj.isNotBlank()) { "CNPJ must not be blank" }
        require(items.isNotEmpty()) { "Items must not be empty"}
    }
}

data class RestaurantItem(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val price: Double
)
