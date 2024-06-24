package br.com.liviafort.deliverysystem.domain.deliveryman


interface DeliverymanRepository {
    fun save(deliveryman: Deliveryman)
    fun findAll(): List<Deliveryman>
}