package br.com.liviafort.deliverysystem.domain.restaurant

class RestaurantServiceImpl: RestaurantService {
    private val restaurants = mutableListOf<Restaurant>()

    override fun create(restaurant: Restaurant) {
        when{
            restaurants.any { it.cnpj == restaurant.cnpj } -> {
                throw IllegalArgumentException("CNPJ already registered") }
            else -> restaurants.add(restaurant)}
    }
}