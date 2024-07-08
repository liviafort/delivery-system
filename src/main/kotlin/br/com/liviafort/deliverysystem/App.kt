package br.com.liviafort.deliverysystem

import br.com.liviafort.deliverysystem.application.resources.ReusableComponents
import br.com.liviafort.deliverysystem.application.submenu.ClientMenuOperations
import br.com.liviafort.deliverysystem.application.submenu.DeliverymanMenuOperations
import br.com.liviafort.deliverysystem.application.submenu.OrderMenuOperations
import br.com.liviafort.deliverysystem.application.submenu.RestaurantMenuOperations

val restaurantMenu = RestaurantMenuOperations()
val clientMenu = ClientMenuOperations()
val deliverymanMenu = DeliverymanMenuOperations()
val orderMenu = OrderMenuOperations()


fun main() {
    // input de teclado
    ReusableComponents()
    var selectedOption = selectMenuOption()

    while (selectedOption != "5") {
        when (selectedOption) {
            "1" -> {
                clientMenu.menu()
            }

            "2" -> {
                restaurantMenu.menu()
            }

            "3" -> {
                deliverymanMenu.menu()
            }

            "4" -> {
                orderMenu.menu()
            }

            else -> {
                println("Opção inválida")
            }
        }
        selectedOption = selectMenuOption()
    }
}


private fun selectMenuOption(): String {
    println("Menu")
    println("1 - Operações de cliente")
    println("2 - Operações de restaurante")
    println("3 - Operações de entregador")
    println("4 - Fazer pedido")
    println("5 - Sair")
    return readlnOrNull() ?: "4"
}
