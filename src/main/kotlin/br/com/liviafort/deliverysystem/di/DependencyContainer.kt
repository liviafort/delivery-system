package br.com.liviafort.deliverysystem.di

import br.com.liviafort.deliverysystem.domain.customer.CustomerRepository
import br.com.liviafort.deliverysystem.domain.deliveryman.DeliverymanRepository
import br.com.liviafort.deliverysystem.domain.order.OrderRepository
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantRepository
import br.com.liviafort.deliverysystem.domain.route.RouteRepository
import br.com.liviafort.deliverysystem.repository.customer.CustomerRepositoryInMemory
import br.com.liviafort.deliverysystem.repository.deliveryman.DeliverymanRepositoryInMemory
import br.com.liviafort.deliverysystem.repository.order.OrderRepositoryInMemory
import br.com.liviafort.deliverysystem.repository.restaurant.RestaurantRepositoryInMemory
import br.com.liviafort.deliverysystem.repository.route.RouteRepositoryInMemory

object DependencyContainer {
    val customerRepository: CustomerRepository by lazy { CustomerRepositoryInMemory() }
    val restaurantRepository: RestaurantRepository by lazy { RestaurantRepositoryInMemory() }
    val deliverymanRepository: DeliverymanRepository by lazy { DeliverymanRepositoryInMemory() }
    val orderRepository: OrderRepository by lazy { OrderRepositoryInMemory(restaurantRepository, customerRepository) }
    val routeRepository: RouteRepository by lazy { RouteRepositoryInMemory(deliverymanRepository, orderRepository)}
}