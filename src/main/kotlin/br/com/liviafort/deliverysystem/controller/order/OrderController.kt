package br.com.liviafort.deliverysystem.controller.order


import br.com.liviafort.deliverysystem.domain.order.Order
import br.com.liviafort.deliverysystem.domain.order.OrderServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: OrderServiceImpl
) {

    @PostMapping
    fun registerNewOrder(@RequestBody order: Order): ResponseEntity<Order> {
        orderService.create(order)
        return ResponseEntity.status(HttpStatus.CREATED).body(order)
    }

    @GetMapping
    fun listOrders(): ResponseEntity<List<Order>> {
        val orders = orderService.listing()
        return ResponseEntity.ok(orders)

    }

    @PostMapping("/cancel")
    fun cancelOrder(@RequestParam("trackingCode", required = true) trackingCode: String): ResponseEntity<Unit> {
        orderService.cancel(trackingCode)
        return ResponseEntity.accepted().build()
    }

    @GetMapping("/{orderId}")
    fun getOrderById(@PathVariable orderId: UUID): ResponseEntity<Order> {
        val order = orderService.getOrder(orderId)
        return ResponseEntity.ok(order)
    }
}
