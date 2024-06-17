package br.com.liviafort.deliverysystem.application.submenu

import br.com.liviafort.deliverysystem.domain.deliveryman.Deliveryman
import br.com.liviafort.deliverysystem.domain.deliveryman.DeliverymanServiceImpl

class DeliverymanMenuOperations {
    private val deliverymanService = DeliverymanServiceImpl()

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
        println("Menu de Entregador")
        println("1 - Registrar entregador")
        println("2 - Listar entregadores")
        println("3 - Voltar ao menu inicial")
        return readlnOrNull() ?: "4"
    }

    private fun listDeliverymen() {
        println("Listando entregadores")
        deliverymanService.listing()
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

}