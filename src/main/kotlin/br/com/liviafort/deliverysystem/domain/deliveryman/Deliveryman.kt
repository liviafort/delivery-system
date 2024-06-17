package br.com.liviafort.deliverysystem.domain.deliveryman

import java.util.*

data class Deliveryman(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val phone: String,
    val vehicle: String
) {
    init {
        require(name.isNotBlank()) { "Name must not be blank" }
        require(phone.isNotBlank()) { "Phone must not be blank" }
        require(vehicle.isNotBlank()) { "Vehicle must not be blank" }
    }
}
