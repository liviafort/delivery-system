package br.com.liviafort.deliverysystem

import br.com.liviafort.deliverysystem.domain.client.Customer
import br.com.liviafort.deliverysystem.domain.client.CustomerServiceImpl

val clientService = CustomerServiceImpl()

fun main() {
    // input de teclado
    var selectedOption = selectMenuOption()

    while (selectedOption != "4") {
        when (selectedOption) {
            "1" -> {
                registerNewClient()
            }
            "2" -> {
                println("Cadastrando restaurante")
            }
            "3" -> {
                println("Cadastrando entregador")
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
    println("2 - Cadastrar entregador")
    println("3 - Fazer pedido")
    println("4 - Sair")
    return readlnOrNull() ?: "4"
}
