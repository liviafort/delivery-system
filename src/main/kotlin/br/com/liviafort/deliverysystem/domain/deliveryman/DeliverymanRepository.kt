package br.com.liviafort.deliverysystem.domain.deliveryman

import java.util.UUID

interface DeliverymanRepository {
    fun save(deliveryman: Deliveryman)
    fun findOne(deliverymanId: UUID): Deliveryman
    fun findAll(): List<Deliveryman>
}