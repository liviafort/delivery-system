package br.com.liviafort.deliverysystem.application.resources

import br.com.liviafort.deliverysystem.domain.customer.Customer
import br.com.liviafort.deliverysystem.domain.deliveryman.Deliveryman
import br.com.liviafort.deliverysystem.domain.restaurant.Restaurant
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantItem


class ReusableComponents() {
    private val customerService = CustomerServiceSingleton.instance
    private val restaurantService = RestaurantServiceSingleton.instance
    private val deliverymanService = DeliverymanServiceSingleton.instance

    init {
        createRestaurant()
        createCustomer()
        createDeliveryman()
    }

    private fun createRestaurant() {
        val items1 = mutableSetOf(
            RestaurantItem(name = "Pizza Quatro Queijos", price = 49.60),
            RestaurantItem(name = "Pizza Diavola", price = 47.20),
        )
        val items2 = mutableSetOf(RestaurantItem(name = "Double cheese", price = 49.60))

        val restaurant1 = Restaurant(
            name = "Pizzaria Straj",
            address = "Rua Alemida Junior, 45",
            category = "Pizzaria",
            cnpj = "38293/0003-123",
            items = items1
        )
        val restaurant2 = Restaurant(
            name = "Hamburgueria Aureau",
            address = "Rua Caimbra, 121",
            category = "Hamburgueria",
            cnpj = "33493/086786-123",
            items = items2
        )

        restaurantService.create(restaurant1)
        restaurantService.create(restaurant2)
    }

    private fun createCustomer() {
        val customer1 = Customer(name = "livia", phone = "12345678", address = "Miguel Seabra, 324")
        val customer2 = Customer(name = "maria", phone = "921749124", address = "Aluisio Mendes, 324")

        customerService.create(customer1)
        customerService.create(customer2)

    }

    private fun createDeliveryman() {
        val deliveryman1 = Deliveryman(name = "Jeremias", phone = "829134342", vehicle = "Car")
        val deliveryman2 = Deliveryman(name = "Rivaldo", phone = "238423243", vehicle = "Motocicleta")

        deliverymanService.create(deliveryman1)
        deliverymanService.create(deliveryman2)
    }
}

