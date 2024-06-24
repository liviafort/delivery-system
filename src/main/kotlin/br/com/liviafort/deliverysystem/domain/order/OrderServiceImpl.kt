package br.com.liviafort.deliverysystem.domain.order

class OrderServiceImpl(private val repository: OrderRepository): OrderService {
    override fun create(order: Order) {
        repository.save(order)
    }

    override fun listing(): List<Order> {
        return repository.findAll()
    }

    override fun cancel(trackingCode: String) {
        repository.remove(trackingCode)
    }
}