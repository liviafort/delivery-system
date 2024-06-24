package br.com.liviafort.deliverysystem.domain.restaurant

class RestaurantServiceImpl(private val repository: RestaurantRepository): RestaurantService {

    override fun create(restaurant: Restaurant) {
        repository.save(restaurant)
    }

    override fun listing(): List<Restaurant> {
        return repository.findAll()
    }
}