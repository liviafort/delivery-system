package br.com.liviafort.deliverysystem.application.submenu

import br.com.liviafort.deliverysystem.application.resources.OrderServiceSingleton
import br.com.liviafort.deliverysystem.deliverymanMenu
import br.com.liviafort.deliverysystem.domain.order.Order
import br.com.liviafort.deliverysystem.domain.order.OrderItem
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantItem

class OrderMenuOperations {
    private val orderService = OrderServiceSingleton.instance
    private val customerMenuOperation = CustomerMenuOperations()
    private val routeMenuOperations = RouteMenuOperations()
    private val restaurantMenuOperation = RestaurantMenuOperations()

    fun menu() {
        var selectedOption = selectMenuOption()

        while (true) {
            when (selectedOption) {
                "1" -> {
                    registerNewOrder()
                }

                "2" -> {
                    listOrders()
                }

                "3" -> {
                    cancelOrder()
                }

                "4" -> {
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
        println("Menu de Pedido")
        println("1 - Registrar pedido")
        println("2 - Listar todos os pedidos")
        println("3 - Cancelar pedido")
        println("4 - Voltar ao menu inicial")
        return readlnOrNull() ?: "4"
    }

    private fun cancelOrder() {
        println("Digite o código do seu pedido:")
        val trackingCode = readln()
        orderService.cancel(trackingCode)
    }

    private fun listOrders() {
        val orders = orderService.listing()
        if (orders.isEmpty()) {
            println("Não há pedidos disponíveis.")
        } else {
            println("Lista de Pedidos:")
            println("ID\t\t\t\t\t\t| Código\t| Preço Total\t| Cliente\t| Restaurante\t|")
            println("---------------------------------------------------------------------------------------------------")
            orders.forEach { order ->
                println("${order.id}\t| ${order.trackingCode}\t| ${order.totalPrice}\t| ${order.customer.name}\t| ${order.restaurant.name}\t|")
            }
            println("---------------------------------------------------------------------------------------------------\n")
        }
    }

    private fun registerNewOrder() {
        println("Fazendo pedido...")
        println("Por favor selecione um restaurante que deseja comprar...")

        val restaurant = restaurantMenuOperation.getOrderRestaurant()
        println("Você selecionou: ${restaurant.name}")
        println("Por favor selecione os itens e a quantidade que deseja")

        val items = getOrderItems(restaurant.items.toList())

        val customer = customerMenuOperation.getOrderCustomer()

        val order= Order(
            items = items,
            restaurant = restaurant,
            customer = customer
        )
        orderService.create(order)
        println("Pedido foi salvo com sucesso, deseja seguir adiante com sua escolhas?")
        acceptOrCancelOrder(order)
    }


    private fun getOrderItems(restaurantItems: List<RestaurantItem>): List<OrderItem> {
        val listOrderItem = mutableListOf<OrderItem>()

        while (true) {
            restaurantItems.forEachIndexed { index, item ->
                println("${index + 1}. ${item.name} - Preço: ${item.price}")
            }
            println("Escolha um item pelo número (ou digite 'sair' para finalizar):")
            val input = readlnOrNull()

            if (input.equals("sair", ignoreCase = true)) {
                break
            }
            val itemNumber = input?.toIntOrNull()

            if (itemNumber != null && itemNumber in 1..restaurantItems.size) {
                val selectedItem = restaurantItems[itemNumber - 1]
                println("Quantidade desejada para ${selectedItem.name}:")
                val quantity = readlnOrNull()?.toIntOrNull() ?: 1

                listOrderItem.add(OrderItem(selectedItem, quantity))
                println("Adicionado: ${selectedItem.name} x$quantity")
                println("Deseja mais alguma coisa?")
            } else {
                println("Seleção inválida. Por favor, escolha um número válido.")
            }
        }
        return listOrderItem
    }

    private fun printOrderDetails(order: Order) {
        println("Detalhes do Pedido:")
        println("ID do Pedido:\t\t\t${order.id}")
        println("Código de Rastreamento:\t${order.trackingCode}")
        println("Restaurante:\t\t\t${order.restaurant.name}")
        println("Cliente:\t\t\t\t${order.customer.name}")
        println("Itens do Pedido:")
        println("Nome do Item\t\t\tQuantidade\tPreço Unitário\tSubtotal")
        println("--------------------------------------------------------------------------------")
        order.items.forEach { item ->
            val itemName = item.restaurantItem.name
            val quantity = item.quantity
            val price = item.restaurantItem.price
            val subtotal = quantity * price
            println("$itemName\t\t\t$quantity\t\t$price\t\t\t$subtotal")
        }
        println("--------------------------------------------------------------------------------")
        println("Preço Total:\t\t\t${order.totalPrice}")
    }

    private fun acceptOrCancelOrder(order: Order) {
        printOrderDetails(order)
        println("Você deseja aceitar (A) ou cancelar (C) o pedido? Digite 'A' para aceitar ou 'C' para cancelar.")
        val choice = readln().uppercase()

        when (choice) {
            "A" -> {
                val deliveryman = deliverymanMenu.selectDeliveryman()
                routeMenuOperations.registerNewRoute(order.id, deliveryman.id)
            }
            "C" -> orderService.cancel(order.trackingCode)
            else -> {
                println("Opção inválida. Por favor, digite 'A' para aceitar ou 'C' para cancelar.")
                acceptOrCancelOrder(order)
            }
        }
    }

}