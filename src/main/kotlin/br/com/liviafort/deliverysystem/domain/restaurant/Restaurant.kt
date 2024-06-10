package br.com.liviafort.deliverysystem.domain.restaurant

import java.util.UUID

data class Restaurant(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val address: String,
    val category: String,
    val cnpj: String,
){
    init {
        require(name.isNotBlank()) { "Name must not be blank" }
        require(address.isNotBlank()) { "Address must not be blank" }
        require(category.isNotBlank()) { "Category must not be blank" }
        require(cnpj.isNotBlank()) { "CNPJ must not be blank" }
    }
}
