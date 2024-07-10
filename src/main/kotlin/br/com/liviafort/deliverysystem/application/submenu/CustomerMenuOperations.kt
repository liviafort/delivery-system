package br.com.liviafort.deliverysystem.application.submenu

import br.com.liviafort.deliverysystem.application.resources.CustomerServiceSingleton
import br.com.liviafort.deliverysystem.domain.customer.Customer

class CustomerMenuOperations {
    private val customerService = CustomerServiceSingleton.instance

    fun menu() {
        var selectedOption = selectMenuOption()

        while (true) {
            when (selectedOption) {
                "1" -> {
                    registerNewClient()
                }

                "2" -> {
                    listClients()
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
        println("Menu de Cliente")
        println("1 - Registrar cliente")
        println("2 - Listar clientes")
        println("3 - Voltar ao menu inicial")
        return readlnOrNull() ?: "4"
    }

    private fun listClients() {
        val customers = customerService.listing()
        if (customers.isEmpty()) {
            println("Não há clientes disponíveis.")
        } else {
            println("Lista de Clientes:")
            println("ID\t\t\t\t\t\t| Nome\t| Telefone\t| Endereço\t|")
            println("---------------------------------------------------------------------------------------------------")
            customers.forEach { customer ->
                println("${customer.id}\t| ${customer.name}\t| ${customer.phone}\t| ${customer.address}\t| ")
            }
            println("---------------------------------------------------------------------------------------------------\n")
        }
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

    fun getOrderCustomer(): Customer {
        val customers = customerService.listing()
        println("Escolha um número de cliente (1 a ${customers.size}):")
        customers.forEachIndexed { index, customer ->
            println("${index + 1}. ${customer.name}")
        }
        while (true) {
            val choice = readlnOrNull()?.toIntOrNull()

            if (choice != null && choice in 1..customers.size) {
                return customers[choice - 1]
            } else {
                println("Seleção inválida. Por favor, escolha um número entre 1 e ${customers.size}.")
            }
        }
    }
}