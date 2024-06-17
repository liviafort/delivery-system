package br.com.liviafort.deliverysystem.domain.deliveryman

interface DeliverymanService {
    fun create(deliveryman: Deliveryman)
    fun listing()
}