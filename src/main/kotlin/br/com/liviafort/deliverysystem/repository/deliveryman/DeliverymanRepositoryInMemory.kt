package br.com.liviafort.deliverysystem.repository.deliveryman

import br.com.liviafort.deliverysystem.domain.deliveryman.Deliveryman
import br.com.liviafort.deliverysystem.domain.deliveryman.DeliverymanRepository
import br.com.liviafort.deliverysystem.domain.exception.EntityAlreadyExistsException

class DeliverymanRepositoryInMemory: DeliverymanRepository {
    private val deliverymen = mutableMapOf<String, Deliveryman>()

    override fun save(deliveryman: Deliveryman) {
        if(deliverymen.containsKey(deliveryman.phone)) {
            throw EntityAlreadyExistsException("Deliveryman phone number (${deliveryman.phone} already exists")
        }
        deliverymen[deliveryman.phone] = deliveryman
    }

    override fun findAll(): List<Deliveryman> {
        return deliverymen.values.toList()
    }
}