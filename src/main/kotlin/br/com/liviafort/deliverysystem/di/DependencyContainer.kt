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
import org.springframework.jdbc.core.JdbcTemplate

object DependencyContainer {
    val customerRepository: CustomerRepository by lazy { CustomerRepositoryImpl(JdbcTemplate()) }
    val restaurantRepository: RestaurantRepository by lazy { RestaurantRepositoryImpl(JdbcTemplate()) }
    val deliverymanRepository: DeliverymanRepository by lazy { DeliverymanRepositoryImpl(JdbcTemplate()) }
    val orderRepository: OrderRepository by lazy { OrderRepositoryImpl(JdbcTemplate(), restaurantRepository, customerRepository) }
    val routeRepository: RouteRepository by lazy { RouteRepositoryImpl(JdbcTemplate(), deliverymanRepository, orderRepository)}
}
