package br.com.liviafort.deliverysystem.application.submenu

import br.com.liviafort.deliverysystem.domain.restaurant.Restaurant
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantServiceImpl
import br.com.liviafort.deliverysystem.repository.restaurant.RestaurantRepositoryInMemory

class RestaurantMenuOperations {
    private val restaurantService = RestaurantServiceImpl(repository = RestaurantRepositoryInMemory())

    fun menu() {
        var selectedOption = selectMenuOption()
        while (true) {
            when (selectedOption) {
                "1" -> {
                    registerNewRestaurant()
                }

                "2" -> {
                    listRestaurants()
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
        println("Menu de Restaurante")
        println("1 - Registrar restaurante")
        println("2 - Listar restaurantes")
        println("3 - Voltar ao menu inicial")
        return readlnOrNull() ?: "4"
    }

    private fun listRestaurants() {
        println("Listando restaurantes")
        restaurantService.listing()
    }

    private fun registerNewRestaurant() {
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
}