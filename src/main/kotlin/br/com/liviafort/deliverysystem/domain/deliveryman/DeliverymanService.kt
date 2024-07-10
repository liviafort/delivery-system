package br.com.liviafort.deliverysystem.domain.deliveryman

import java.util.UUID

interface DeliverymanService {
    fun create(deliveryman: Deliveryman)
    fun getDeliveryman(deliverymanId: UUID): Deliveryman
    fun listing(): List<Deliveryman>
}