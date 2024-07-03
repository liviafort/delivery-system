package br.com.liviafort.deliverysystem.domain.restaurant

interface RestaurantService {
    fun create(restaurant: Restaurant)
    fun listing(): List<Restaurant>
    fun addItem(restaurant: Restaurant, restaurantItem: RestaurantItem)
}