package br.com.liviafort.deliverysystem.domain.restaurant

interface RestaurantRepository {
    fun save(restaurant: Restaurant)
    fun findAll(): List<Restaurant>
}