package br.com.liviafort.deliverysystem.domain.restaurant

import java.util.UUID

interface RestaurantRepository {
    fun save(restaurant: Restaurant)
    fun findOne(restaurantId: UUID): Restaurant
    fun findAll(): List<Restaurant>
    fun insertItem(restaurantId: UUID, restaurantItem: RestaurantItem)
    fun findAllItems(restaurantId: UUID): List<RestaurantItem>
}