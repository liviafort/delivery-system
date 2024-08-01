package br.com.liviafort.deliverysystem.application.submenu

import br.com.liviafort.deliverysystem.di.DependencyContainer
import br.com.liviafort.deliverysystem.domain.deliveryman.DeliverymanService
import br.com.liviafort.deliverysystem.domain.deliveryman.DeliverymanServiceImpl
import br.com.liviafort.deliverysystem.domain.order.OrderServiceImpl
import br.com.liviafort.deliverysystem.domain.route.Route
import br.com.liviafort.deliverysystem.domain.route.RouteServiceImpl
import br.com.liviafort.deliverysystem.domain.route.RouteStatus
import br.com.liviafort.deliverysystem.repository.customer.CustomerRepositoryInMemory
import br.com.liviafort.deliverysystem.repository.deliveryman.DeliverymanRepositoryInMemory
import br.com.liviafort.deliverysystem.repository.order.OrderRepositoryInMemory
import br.com.liviafort.deliverysystem.repository.restaurant.RestaurantRepositoryInMemory
import br.com.liviafort.deliverysystem.repository.route.RouteRepositoryInMemory
import java.util.UUID

class RouteMenuOperations {
    private val routeService = RouteServiceImpl(DependencyContainer.routeRepository)
    private val deliverymanService = DeliverymanServiceImpl(DependencyContainer.deliverymanRepository)
    private val orderService = OrderServiceImpl(DependencyContainer.orderRepository)

    fun menu() {
        var selectedOption = selectMenuOption()
        while (true) {
            when (selectedOption) {
                "1" -> {
                    listRoutes()
                }

                "2" -> {
                    finishRoute()
                }

                "3" -> {
                    break
                }

                else -> {
                    println("Opção inválida")
                }
            }
            selectedOption = selectMenuOption()
        }
    }

    private fun selectMenuOption(): String {
        println("Menu de Rota")
        println("1 - Listar rotas")
        println("2 - Confirmar rota finalizada")
        println("3 - Voltar ao menu inicial")
        return readlnOrNull() ?: "4"
    }

    private fun finishRoute() {
        println("Confirme sua entrega... digite o código do seu pedido por favor!")

        val trackingCode = readln()
        val route = routeService.getRouteByTrackingCode(trackingCode)
        routeService.changeStatus(route.id, RouteStatus.FINISHED)
        println("Obrigada pela preferência, volte sempre!")
    }

    fun registerNewRoute(orderId: UUID, deliverymanId: UUID) {
        val order = orderService.getOrder(orderId)
        val deliveryman = deliverymanService.getDeliveryman(deliverymanId)
        println("Estamos quase lá, digite o destino (endereço) do seu pedido.")

        val destination = readln()

        val route = Route(
            destination = destination,
            deliveryman = deliveryman,
            order = order
        )

        routeService.create(route)

        println("Pedido finalizado com sucesso! Vá para o menu de rota assim que receber seu pedido e confirme sua entrega.")
    }

    private fun listRoutes() {
        val routes = routeService.listing()
        if (routes.isEmpty()) {
            println("Não há rotas disponíveis.")
        } else {
            println("Lista de Rotas:")
            println("ID\t\t\t\t\t\t| Destino\t| Entregador\t| Pedido\t| Status\t| Identificador")
            println("---------------------------------------------------------------------------------------------------")
            routes.forEach { route ->
                println("${route.id}\t| ${route.destination}\t| ${route.deliveryman.name}\t| ${route.order.id}\t| ${route.status}\t| ${route.identifier}")
            }
            println("---------------------------------------------------------------------------------------------------\n")
        }
    }
}