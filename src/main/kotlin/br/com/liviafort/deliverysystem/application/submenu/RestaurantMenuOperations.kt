package br.com.liviafort.deliverysystem.application.submenu

import br.com.liviafort.deliverysystem.domain.restaurant.Restaurant
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantItem
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

    private fun addItemsToRestaurant(items: MutableSet<RestaurantItem>) {
        do {
            println("Nome do item:")
            val itemName = readln()
            println("Preço do item:")
            val itemPrice = readln().toDouble()
            items.add(RestaurantItem(name = itemName, price = itemPrice))
            println("Item adicionado. Deseja adicionar outro item (s/n)?")
            val answer = readln()
        } while (answer.lowercase() == "s")

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

        val items = mutableSetOf<RestaurantItem>()
        println("Por favor, adicione pelo menos um item ao menu.")
        addItemsToRestaurant(items)

        val restaurant = Restaurant(name = name, address = address, category = category, cnpj = cnpj, items = items)
        try {
            restaurantService.create(restaurant)
            println("Restaurante cadastrado com sucesso")
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }

}