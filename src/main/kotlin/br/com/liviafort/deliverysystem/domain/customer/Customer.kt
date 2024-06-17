package br.com.liviafort.deliverysystem.domain.customer

import java.util.*

data class Customer(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val phone: String,
    val address: String,
) {
    init {
        require(name.isNotBlank()) { "Name must not be blank" }
        require(phone.isNotBlank()) { "Phone must not be blank" }
        require(address.isNotBlank()) { "Address must not be blank" }
    }
}
