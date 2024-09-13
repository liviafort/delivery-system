package br.com.liviafort.deliverysystem.domain.order

import java.util.*
import org.springframework.stereotype.Service

@Service
class OrderServiceImpl(private val repository: OrderRepository): OrderService {
    override fun create(order: Order) {
        repository.save(order)
    }
    override fun getOrder(orderId: UUID): Order {
        return repository.findOne(orderId)
    }

    override fun listing(): List<Order> {
        return repository.findAll()
    }

    override fun cancel(trackingCode: String) {
        repository.remove(trackingCode)
    }
}