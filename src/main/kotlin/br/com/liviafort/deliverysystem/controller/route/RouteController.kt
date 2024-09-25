package br.com.liviafort.deliverysystem.controller.route

import br.com.liviafort.deliverysystem.domain.route.Route
import br.com.liviafort.deliverysystem.domain.route.RouteServiceImpl
import br.com.liviafort.deliverysystem.domain.route.RouteStatus
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/routes")
class RouteController(
    private val routeService: RouteServiceImpl
) {

    @PostMapping
    fun registerNewRoute(@RequestBody route: Route): ResponseEntity<Route> {
        routeService.create(route)
        return ResponseEntity.status(HttpStatus.CREATED).body(route)
    }

    @GetMapping
    fun listRoutes(): ResponseEntity<List<Route>> {
        val routes = routeService.listing()
        return ResponseEntity.ok(routes)
    }

    @PostMapping("/{trackingCode}/finish")
    fun finishRoute(@PathVariable trackingCode: String): ResponseEntity<String> {
        val route = routeService.getRouteByTrackingCode(trackingCode)
        routeService.changeStatus(route.id, RouteStatus.FINISHED)
        return ResponseEntity.ok("Rota finalizada com sucesso!")
    }

    @GetMapping("/{routeId}")
    fun getRouteById(@PathVariable routeId: UUID): ResponseEntity<Route> {
        val route = routeService.getRoute(routeId)
        return ResponseEntity.ok(route)
    }
}
