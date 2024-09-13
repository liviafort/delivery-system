package br.com.liviafort.deliverysystem.domain.route

import br.com.liviafort.deliverysystem.domain.deliveryman.Deliveryman
import br.com.liviafort.deliverysystem.domain.order.Order
import java.util.UUID
import kotlin.random.Random

data class Route(
    val id: UUID = UUID.randomUUID(),
    val destination: String,
    val deliveryman: Deliveryman,
    val order: Order,
    var status: RouteStatus = RouteStatus.IN_PROGRESS,
) {
    val identifier: String = List(5) { Random.nextInt(0, 10) }.joinToString("")
    init {
        require(destination.isNotBlank()) { "Destination must not be blank" }
    }
}

enum class RouteStatus {
    IN_PROGRESS,
    FINISHED,
    CANCELED
}
