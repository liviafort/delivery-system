package br.com.liviafort.deliverysystem.domain.deliveryman

import java.util.*

class DeliverymanServiceImpl(private val repository: DeliverymanRepository) : DeliverymanService {

    override fun create(deliveryman: Deliveryman) {
        repository.save(deliveryman)
    }

    override fun getDeliveryman(deliverymanId: UUID): Deliveryman {
        return repository.findOne(deliverymanId)
    }

    override fun listing(): List<Deliveryman> {
        return repository.findAll()
    }
}