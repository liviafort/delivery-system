package br.com.liviafort.deliverysystem.di

import br.com.liviafort.deliverysystem.domain.customer.CustomerRepository
import br.com.liviafort.deliverysystem.domain.deliveryman.DeliverymanRepository
import br.com.liviafort.deliverysystem.domain.order.OrderRepository
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantRepository
import br.com.liviafort.deliverysystem.domain.route.RouteRepository
import br.com.liviafort.deliverysystem.repository.customer.CustomerRepositoryImpl
import br.com.liviafort.deliverysystem.repository.deliveryman.DeliverymanRepositoryImpl
import br.com.liviafort.deliverysystem.repository.order.OrderRepositoryImpl
import br.com.liviafort.deliverysystem.repository.restaurant.RestaurantRepositoryImpl
import br.com.liviafort.deliverysystem.repository.route.RouteRepositoryImpl

object DependencyContainer {
    val customerRepository: CustomerRepository by lazy { CustomerRepositoryImpl() }
    val restaurantRepository: RestaurantRepository by lazy { RestaurantRepositoryImpl() }
    val deliverymanRepository: DeliverymanRepository by lazy { DeliverymanRepositoryImpl() }
    val orderRepository: OrderRepository by lazy { OrderRepositoryImpl(restaurantRepository, customerRepository) }
    val routeRepository: RouteRepository by lazy { RouteRepositoryImpl(deliverymanRepository, orderRepository)}
}