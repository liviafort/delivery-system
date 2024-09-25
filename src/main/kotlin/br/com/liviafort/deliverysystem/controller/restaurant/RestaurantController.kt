package br.com.liviafort.deliverysystem.controller.restaurant

import br.com.liviafort.deliverysystem.domain.restaurant.Restaurant
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantItem
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/restaurants")
class RestaurantController(
    private val restaurantService: RestaurantServiceImpl
) {

    @PostMapping
    fun registerNewRestaurant(@RequestBody restaurant: Restaurant): ResponseEntity<Restaurant> {
        restaurantService.create(restaurant)
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurant)
    }

    @GetMapping
    fun listRestaurants(): ResponseEntity<List<Restaurant>> {
        val restaurants = restaurantService.listing()
        return ResponseEntity.ok(restaurants)
    }

    @GetMapping("/{restaurantId}")
    fun getRestaurantById(@PathVariable restaurantId: UUID): ResponseEntity<Restaurant> {
        val restaurant = restaurantService.getRestaurant(restaurantId)
        return ResponseEntity.ok(restaurant)
    }

    @PostMapping("/{restaurantId}/items")
    fun addItemToRestaurant(@PathVariable restaurantId: UUID, @RequestBody restaurantItem: RestaurantItem): ResponseEntity<String> {
        restaurantService.addItem(restaurantId, restaurantItem)
        return ResponseEntity.ok("Item ${restaurantItem.name} foi adicionado ao restaurante com sucesso")
    }

    @GetMapping("/{restaurantId}/items")
    fun listRestaurantItems(@PathVariable restaurantId: UUID): ResponseEntity<List<RestaurantItem>> {
        val items = restaurantService.listingItems(restaurantId)
        return ResponseEntity.ok(items)
    }
}
