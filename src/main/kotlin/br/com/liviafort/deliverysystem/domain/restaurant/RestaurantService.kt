package br.com.liviafort.deliverysystem.domain.restaurant

import java.util.UUID

interface RestaurantService {
    fun create(restaurant: Restaurant)
    fun listing(): List<Restaurant>
    fun getRestaurant(restaurantId: UUID): Restaurant
    fun addItem(restaurantId: UUID, restaurantItem: RestaurantItem)
    fun listingItems(restaurantId: UUID): List<RestaurantItem>
}