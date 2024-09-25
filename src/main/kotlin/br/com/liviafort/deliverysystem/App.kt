package br.com.liviafort.deliverysystem

import br.com.liviafort.deliverysystem.application.submenu.CustomerMenuOperations
import br.com.liviafort.deliverysystem.application.submenu.DeliverymanMenuOperations
import br.com.liviafort.deliverysystem.application.submenu.OrderMenuOperations
import br.com.liviafort.deliverysystem.application.submenu.RestaurantMenuOperations
import br.com.liviafort.deliverysystem.application.submenu.RouteMenuOperations
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class App : CommandLineRunner {

    val restaurantMenu = RestaurantMenuOperations()
    val clientMenu = CustomerMenuOperations()
    val deliverymanMenu = DeliverymanMenuOperations()
    val orderMenu = OrderMenuOperations()
    val routeMenu = RouteMenuOperations()


    override fun run(vararg args: String?) {
//        ReusableComponents()
//        var selectedOption = selectMenuOption()
//
//        while (selectedOption != "6") {
//            when (selectedOption) {
//                "1" -> {
//                    clientMenu.menu()
//                }
//                "2" -> {
//                    restaurantMenu.menu()
//                }
//                "3" -> {
//                    deliverymanMenu.menu()
//                }
//                "4" -> {
//                    orderMenu.menu()
//                }
//                "5" -> {
//                    routeMenu.menu()
//                }
//                else -> {
//                    println("Opção inválida")
//                }
//            }
//            selectedOption = selectMenuOption()
//        }
//    }
//
//    private fun selectMenuOption(): String {
//        println("Menu")
//        println("1 - Operações de cliente")
//        println("2 - Operações de restaurante")
//        println("3 - Operações de entregador")
//        println("4 - Fazer pedido")
//        println("5 - Operações de rota")
//        println("6 - Sair")
//        return readlnOrNull() ?: "6"
    }
}

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
