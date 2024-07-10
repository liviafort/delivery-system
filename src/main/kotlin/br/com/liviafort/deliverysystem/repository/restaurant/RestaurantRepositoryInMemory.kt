package br.com.liviafort.deliverysystem.repository.restaurant

import br.com.liviafort.deliverysystem.domain.exception.EntityAlreadyExistsException
import br.com.liviafort.deliverysystem.domain.restaurant.Restaurant
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantItem
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantRepository
import java.util.*

class RestaurantRepositoryInMemory: RestaurantRepository {
    private val restaurants = mutableMapOf<String, Restaurant>()

    override fun save(restaurant: Restaurant) {
        if(restaurants.containsKey(restaurant.cnpj)){
            throw EntityAlreadyExistsException("Restaurant cnpj number (${restaurant.cnpj} already exists")
        }
        restaurants[restaurant.cnpj] = restaurant
    }

    override fun findOne(restaurantId: UUID): Restaurant {
        return restaurants.values.first { it.id == restaurantId }
    }

    override fun findAll(): List<Restaurant> {
        return restaurants.values.toList()
    }

    override fun insertItem(restaurantId: UUID,restaurantItem: RestaurantItem) {
        val restaurant = findOne(restaurantId)
        restaurant.items.add(restaurantItem)
    }

    override fun findAllItems(restaurantId: UUID): List<RestaurantItem> {
        return findOne(restaurantId).items.toList()
    }
}