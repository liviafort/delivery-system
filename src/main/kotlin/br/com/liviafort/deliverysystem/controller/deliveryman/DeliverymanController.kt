package br.com.liviafort.deliverysystem.controller.deliveryman

import br.com.liviafort.deliverysystem.domain.deliveryman.Deliveryman
import br.com.liviafort.deliverysystem.domain.deliveryman.DeliverymanServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/deliverymen")
class DeliverymanController(
    private val deliverymanService: DeliverymanServiceImpl
) {

    @GetMapping
    fun listDeliverymen(): ResponseEntity<List<Deliveryman>> {
        val deliverymen = deliverymanService.listing()
        return ResponseEntity.ok(deliverymen)
    }

    @PostMapping
    fun registerNewDeliveryman(@RequestBody deliveryman: Deliveryman): ResponseEntity<Deliveryman> {
        deliverymanService.create(deliveryman)
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryman)
    }

    @GetMapping("/{id}")
    fun getDeliverymanById(@PathVariable id: UUID): ResponseEntity<Deliveryman> {
        val deliveryman = deliverymanService.getDeliveryman(id)
        return ResponseEntity.ok(deliveryman)
    }
}
