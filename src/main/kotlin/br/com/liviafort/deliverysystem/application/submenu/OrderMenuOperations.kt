package br.com.liviafort.deliverysystem.application.submenu

import br.com.liviafort.deliverysystem.domain.order.Order
import br.com.liviafort.deliverysystem.domain.order.OrderItem
import br.com.liviafort.deliverysystem.domain.order.OrderServiceImpl
import br.com.liviafort.deliverysystem.repository.order.OrderRepositoryInMemory
import java.util.*
import kotlin.random.Random


class OrderMenuOperations {
    private val orderService = OrderServiceImpl(repository = OrderRepositoryInMemory())

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
        println("2 - Listar itens do pedido")
        println("3 - Listar todos os pedidos")
        println("4 - Cancelar pedido")
        println("5 - Voltar ao menu inicial")
        return readlnOrNull() ?: "4"
    }

    private fun cancelOrder() {
        println("Digite o código do seu pedido:")
        val trackingCode = readln()
        orderService.cancel(trackingCode)
    }

    private fun listOrders() {
        println("Listando todos os pedidos")
        println(orderService.listing())
    }

    private fun registerNewOrder() {
        println("Fazendo pedido...")

        val items = selectItems()

        if (items.isEmpty()) {
            println("Nenhum produto foi adicionado ao pedido. Cancelando...")
            return
        }

        val totalPrice = items.sumOf { it.quantity * it.price }

        val order = Order(
            id = UUID.randomUUID(),
            items = items,
            trackingCode = List(10) { Random.nextInt(0, 10) }.joinToString(""),
            totalPrice = totalPrice,
        )
        orderService.create(order)
        println("Pedido feito com sucesso")
    }

    private fun selectItems(): List<OrderItem> {
        val items = mutableListOf<OrderItem>()

        while (true) {
            println("Digite o nome do produto ou 'sair' para finalizar:")
            val productName = readln()
            if (productName.lowercase() == "sair") break

            println("Digite a quantidade:")
            val quantity = readln().toInt()

            println("Digite o preço unitário:")
            val price = readln().toDouble()

            items.add(OrderItem(productId = productName, quantity = quantity, price = price))

            println("Produto adicionado ao pedido.")
        }

        return items
    }
}