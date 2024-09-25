package br.com.liviafort.deliverysystem.repository.restaurant

import br.com.liviafort.deliverysystem.domain.restaurant.Restaurant
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantItem
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class RestaurantRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
) : RestaurantRepository {

    private val restaurantRowMapper = RowMapper { rs, _ ->
        Restaurant(
            id = rs.getObject("id", UUID::class.java),
            name = rs.getString("name"),
            address = rs.getString("address"),
            category = rs.getString("category"),
            cnpj = rs.getString("cnpj"),
            items = findAllItems(rs.getObject("id", UUID::class.java)).toMutableSet()
        )
    }

    private val restaurantItemRowMapper = RowMapper<RestaurantItem> { rs, _ ->
        RestaurantItem(
            id = rs.getObject("id", UUID::class.java),
            name = rs.getString("name"),
            price = rs.getDouble("price")
        )
    }

    override fun save(restaurant: Restaurant) {
        val sql = "INSERT INTO restaurant (id, name, address, category, cnpj) VALUES (?, ?, ?, ?, ?)"
        val itemSql = "INSERT INTO restaurant_item (id, restaurant_id, name, price) VALUES (?, ?, ?, ?)"

        try {
            // Insere o restaurante
            jdbcTemplate.update(sql, restaurant.id, restaurant.name, restaurant.address, restaurant.category, restaurant.cnpj)

            // Insere os itens do restaurante
            restaurant.items.forEach { item ->
                jdbcTemplate.update(itemSql, item.id, restaurant.id, item.name, item.price)
            }
        } catch (e: Exception) {
            if (e.message?.contains("duplicate key value violates unique constraint \"restaurant_cnpj_key\"") == true) {
                throw RuntimeException("Error saving restaurant: CNPJ already exists", e)
            } else {
                throw RuntimeException("Error saving restaurant", e)
            }
        }
    }

    override fun findOne(restaurantId: UUID): Restaurant {
        val sql = "SELECT * FROM restaurant WHERE id = ?"
        return jdbcTemplate.queryForObject(sql, restaurantRowMapper, restaurantId)
            ?: throw NoSuchElementException("Restaurant with ID $restaurantId not found")
    }

    override fun findAll(): List<Restaurant> {
        val sql = "SELECT * FROM restaurant"
        return jdbcTemplate.query(sql, restaurantRowMapper)
    }

    override fun insertItem(restaurantId: UUID, restaurantItem: RestaurantItem) {
        val sql = "INSERT INTO restaurant_item (id, restaurant_id, name, price) VALUES (?, ?, ?, ?)"
        try {
            jdbcTemplate.update(sql, restaurantItem.id, restaurantId, restaurantItem.name, restaurantItem.price)
        } catch (e: Exception) {
            throw RuntimeException("Error inserting item", e)
        }
    }

    override fun findAllItems(restaurantId: UUID): List<RestaurantItem> {
        val sql = "SELECT * FROM restaurant_item WHERE restaurant_id = ?"
        return jdbcTemplate.query(sql, restaurantItemRowMapper, restaurantId)
    }

    override fun findOneItem(itemId: UUID): RestaurantItem {
        val sql = "SELECT * FROM restaurant_item WHERE id = ?"
        return jdbcTemplate.queryForObject(sql, restaurantItemRowMapper, itemId)
            ?: throw NoSuchElementException("Restaurant item with ID $itemId not found")
    }
}
