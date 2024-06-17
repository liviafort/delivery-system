package br.com.liviafort.deliverysystem.application.submenu

import br.com.liviafort.deliverysystem.domain.client.Customer
import br.com.liviafort.deliverysystem.domain.client.CustomerServiceImpl

class ClientMenuOperations {
    private val customerService = CustomerServiceImpl()

    fun menu() {
        var selectedOption = selectMenuOption()

        while(true){
            when (selectedOption) {
                "1" -> {
                    registerNewClient()
                }
                "2" -> {
                    listClients()
                }
                "3" -> {
                    break;
                }
                else -> {
                    println("Opção inválida")
                }
            }
            selectedOption = selectMenuOption()
        }
    }

    private fun selectMenuOption(): String {
        println("Menu de Cliente")
        println("1 - Registrar cliente")
        println("2 - Listar clientes")
        println("3 - Voltar ao menu inicial")
        return readlnOrNull() ?: "4"
    }

    private fun listClients() {
        println("Listando clientes")
        customerService.listing()
    }

    private fun registerNewClient() {
        println("Cadastrando cliente")
        println("Nome:")
        val name = readln()
        println("Telefone:")
        val phone = readln()
        println("Endereço:")
        val address = readln()

        val customer = Customer(name = name, phone = phone, address = address)
        customerService.create(customer)
        println("Cliente cadastrado com sucesso")
    }
}