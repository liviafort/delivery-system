package br.com.liviafort.deliverysystem.domain.restaurant

class RestaurantServiceImpl: RestaurantService {
    private val restaurants = mutableListOf<Restaurant>()

    override fun create(restaurant: Restaurant) {
        try {
            when{
                restaurants.any { it.cnpj == restaurant.cnpj } -> {
                    throw IllegalArgumentException("CNPJ already registered") }
                else -> restaurants.add(restaurant)}
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }

    override fun listing() {
        restaurants.forEachIndexed { index, restaurant ->
            println("${index + 1} - ${restaurant.name}")
        }
    }
}