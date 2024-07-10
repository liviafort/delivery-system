package br.com.liviafort.deliverysystem.application.submenu

import br.com.liviafort.deliverysystem.application.resources.DeliverymanServiceSingleton
import br.com.liviafort.deliverysystem.domain.deliveryman.Deliveryman
import br.com.liviafort.deliverysystem.domain.deliveryman.DeliverymanServiceImpl
import br.com.liviafort.deliverysystem.repository.deliveryman.DeliverymanRepositoryInMemory
import java.util.UUID

class DeliverymanMenuOperations {
    private val deliverymanService = DeliverymanServiceSingleton.instance

    fun menu() {
        var selectedOption = selectMenuOption()

        while (true) {
            when (selectedOption) {
                "1" -> {
                    registerNewDeliveryman()
                }

                "2" -> {
                    listDeliverymen()
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
        println("Menu de Entregador")
        println("1 - Registrar entregador")
        println("2 - Listar entregadores")
        println("3 - Voltar ao menu inicial")
        return readlnOrNull() ?: "4"
    }

    private fun listDeliverymen() {
        val deliverymen = deliverymanService.listing()
        if (deliverymen.isEmpty()) {
            println("Não há entregadores disponíveis.")
        } else {
            println("Lista de Entregadores:")
            println("ID\t\t\t\t\t\t| Nome\t| Telefone\t| Veículo\t|")
            println("---------------------------------------------------------------------------------------------------")
            deliverymen.forEach { deliveryman ->
                println("${deliveryman.id}\t| ${deliveryman.name}\t| ${deliveryman.phone}\t| ${deliveryman.vehicle}\t|")
            }
            println("---------------------------------------------------------------------------------------------------\n")
        }
    }

    private fun registerNewDeliveryman() {
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

    fun selectDeliveryman(): Deliveryman {
        val deliverymen = deliverymanService.listing()
        println("Escolha um número de entregador (1 a ${deliverymen.size}):")
        deliverymen.forEachIndexed { index, restaurant ->
            println("${index + 1}. ${restaurant.name}")
        }
        while (true) {
            val choice = readlnOrNull()?.toIntOrNull()

            if (choice != null && choice in 1..deliverymen.size) {
                return deliverymen[choice - 1]
            } else {
                println("Seleção inválida. Por favor, escolha um número entre 1 e ${deliverymen.size}.")
            }
        }
    }


}