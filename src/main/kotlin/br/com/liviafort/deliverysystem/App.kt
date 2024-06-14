package br.com.liviafort.deliverysystem

import br.com.liviafort.deliverysystem.domain.client.Customer
import br.com.liviafort.deliverysystem.domain.client.CustomerServiceImpl
import br.com.liviafort.deliverysystem.domain.deliveryman.Deliveryman
import br.com.liviafort.deliverysystem.domain.deliveryman.DeliverymanServiceImpl
import br.com.liviafort.deliverysystem.domain.restaurant.Restaurant
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantServiceImpl

val clientService = CustomerServiceImpl()
val restaurantService = RestaurantServiceImpl()
val deliverymanService = DeliverymanServiceImpl()

fun main() {
    // input de teclado
    var selectedOption = selectMenuOption()

    while (selectedOption != "5") {
        when (selectedOption) {
            "1" -> {
                registerNewClient()
            }
            "2" -> {
                registerNewRestaurant()
            }
            "3" -> {
                registerNewDeliveryman()
            }
            "4" -> {
                println("Fazendo pedido")
            }
            else -> {
                println("Opção inválida")
            }
        }
        selectedOption = selectMenuOption()
    }
}

fun registerNewDeliveryman() {
    println("Cadastrando entregador")
    println("Nome:")
    val name = readln()
    println("Telefone:")
    val phone = readln()
    println("Veículo:")
    val vehicle = readln()

    val deliveryman = Deliveryman(name = name, phone = phone, vehicle = vehicle)
    deliverymanService.create(deliveryman)
    println("Entregador cadastrado com sucesso")
}

fun registerNewRestaurant() {
    println("Cadastrando restaurante")
    println("Nome:")
    val name = readln()
    println("Endereço:")
    val address = readln()
    println("Categoria:")
    val category = readln()
    println("CNPJ:")
    val cnpj = readln()

    val restaurant = Restaurant(name = name, address = address, category = category, cnpj = cnpj)
    restaurantService.create(restaurant)
    println("Restaurante cadastrado com sucesso")
}

private fun registerNewClient() {
    // FIXME tratamento de exception
    println("Cadastrando cliente")
    println("Nome:")
    val name = readln()
    println("Telefone:")
    val phone = readln()
    println("Endereço:")
    val address = readln()
    val customer = Customer(name = name, phone = phone, address = address)
    clientService.create(customer)
    println("Cliente cadastrado com sucesso")
}

private fun selectMenuOption(): String {
    println("Menu")
    // TODO submenus client (cadastras, listar), restaurante (cadastrar, listar), entregador (cadastrar, listar)
    println("1 - Cadastrar cliente")
    println("2 - Cadastrar restaurante")
    println("3 - Cadastrar entregador")
    println("4 - Fazer pedido")
    println("5 - Sair")
    return readlnOrNull() ?: "4"
}
