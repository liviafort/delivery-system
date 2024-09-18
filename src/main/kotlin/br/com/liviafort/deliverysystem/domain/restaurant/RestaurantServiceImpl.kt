package br.com.liviafort.deliverysystem.domain.restaurant

import java.util.UUID
import org.springframework.stereotype.Service

@Service
class RestaurantServiceImpl(private val repository: RestaurantRepository): RestaurantService {

    override fun create(restaurant: Restaurant) {
        repository.save(restaurant)
    }

    override fun getRestaurant(restaurantId: UUID): Restaurant {
        return repository.findOne(restaurantId)
    }

    override fun listing(): List<Restaurant> {
        return repository.findAll()
    }

    override fun addItem(restaurantId: UUID, restaurantItem: RestaurantItem) {
        repository.insertItem(restaurantId, restaurantItem)
    }

    override fun listingItems(restaurantId: UUID): List<RestaurantItem> {
        return repository.findAllItems(restaurantId)
    }
}