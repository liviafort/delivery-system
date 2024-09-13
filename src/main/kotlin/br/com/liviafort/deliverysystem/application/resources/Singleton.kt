package br.com.liviafort.deliverysystem.application.resources

import br.com.liviafort.deliverysystem.domain.customer.CustomerServiceImpl
import br.com.liviafort.deliverysystem.domain.deliveryman.DeliverymanServiceImpl
import br.com.liviafort.deliverysystem.domain.order.OrderServiceImpl
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantServiceImpl
import br.com.liviafort.deliverysystem.domain.route.RouteServiceImpl
import br.com.liviafort.deliverysystem.repository.customer.CustomerRepositoryInMemory
import br.com.liviafort.deliverysystem.repository.deliveryman.DeliverymanRepositoryInMemory
import br.com.liviafort.deliverysystem.repository.order.OrderRepositoryInMemory
import br.com.liviafort.deliverysystem.repository.restaurant.RestaurantRepositoryInMemory
import br.com.liviafort.deliverysystem.repository.route.RouteRepositoryInMemory

object OrderServiceSingleton {
    val instance = OrderServiceImpl(OrderRepositoryInMemory())
}

object RestaurantServiceSingleton {
    val instance = RestaurantServiceImpl(RestaurantRepositoryInMemory())
}

object DeliverymanServiceSingleton {
    val instance = DeliverymanServiceImpl(DeliverymanRepositoryInMemory())
}

object CustomerServiceSingleton {
    val instance = CustomerServiceImpl(CustomerRepositoryInMemory())
}

object RouteServiceSingleton {
    val instance = RouteServiceImpl(RouteRepositoryInMemory())
}