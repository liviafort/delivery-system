package br.com.liviafort.deliverysystem.domain.deliveryman

class DeliverymanServiceImpl: DeliverymanService {

    private val deliverymen = mutableListOf<Deliveryman>()

    override fun create(deliveryman: Deliveryman) {
        when{
            deliverymen.any{ it.phone == deliveryman.phone} -> {
                throw IllegalArgumentException("Phone already registered")
            }
            else -> deliverymen.add(deliveryman)
        }
    }
}