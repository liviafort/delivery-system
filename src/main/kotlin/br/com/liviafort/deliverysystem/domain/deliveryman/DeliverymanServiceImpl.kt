package br.com.liviafort.deliverysystem.domain.deliveryman

class DeliverymanServiceImpl(private val repository: DeliverymanRepository) : DeliverymanService {

    override fun create(deliveryman: Deliveryman) {
        repository.save(deliveryman)
    }

    override fun listing(): List<Deliveryman> {
        return repository.findAll()
    }
}