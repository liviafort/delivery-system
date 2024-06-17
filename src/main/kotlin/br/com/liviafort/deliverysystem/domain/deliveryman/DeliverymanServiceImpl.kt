package br.com.liviafort.deliverysystem.domain.deliveryman

class DeliverymanServiceImpl : DeliverymanService {

    private val deliverymen = mutableListOf<Deliveryman>()

    override fun create(deliveryman: Deliveryman) {
        try {
            when {
                deliverymen.any { it.phone == deliveryman.phone } -> {
                    throw IllegalArgumentException("Phone already registered")
                }

                else -> deliverymen.add(deliveryman)
            }
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }

    override fun listing() {
        deliverymen.forEachIndexed { index, deliveryman ->
            println("${index + 1} - ${deliveryman.name}")
        }
    }
}