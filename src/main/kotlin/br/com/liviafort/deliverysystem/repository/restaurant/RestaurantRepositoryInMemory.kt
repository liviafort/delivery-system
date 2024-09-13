package br.com.liviafort.deliverysystem.repository.restaurant

import br.com.liviafort.deliverysystem.config.DatabaseConfig
import br.com.liviafort.deliverysystem.domain.restaurant.Restaurant
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantItem
import br.com.liviafort.deliverysystem.domain.restaurant.RestaurantRepository
import java.sql.SQLException
import java.util.*

class RestaurantRepositoryInMemory: RestaurantRepository {

    override fun save(restaurant: Restaurant) {
        val sql = "INSERT INTO restaurant (id, name, address, category, cnpj) VALUES (?, ?, ?, ?, ?)"
        val itemSql = "INSERT INTO restaurant_item (id, restaurant_id, name, price) VALUES (?, ?, ?, ?)"
        DatabaseConfig.getConnection().use { connection ->
            connection.autoCommit = false
            try {
                connection.prepareStatement(sql).use { statement ->
                    statement.setObject(1, restaurant.id)
                    statement.setString(2, restaurant.name)
                    statement.setString(3, restaurant.address)
                    statement.setString(4, restaurant.category)
                    statement.setString(5, restaurant.cnpj)
                    statement.executeUpdate()
                }

                connection.prepareStatement(itemSql).use { itemStatement ->
                    for (item in restaurant.items) {
                        itemStatement.setObject(1, item.id)
                        itemStatement.setObject(2, restaurant.id)
                        itemStatement.setString(3, item.name)
                        itemStatement.setDouble(4, item.price)
                        itemStatement.addBatch()
                    }
                    itemStatement.executeBatch()
                }

                connection.commit()
            } catch (e: SQLException) {
                connection.rollback()
                if (e.message?.contains("duplicate key value violates unique constraint \"restaurant_cnpj_key\"") == true) {
                    throw RuntimeException("Error saving restaurant: CNPJ already exists", e)
                } else {
                    throw RuntimeException("Error saving restaurant", e)
                }
            }
        }
    }



    override fun findOne(restaurantId: UUID): Restaurant {
        val sql = "SELECT * FROM restaurant WHERE id = ?"
        val connection = DatabaseConfig.getConnection()
        try {
            val preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setObject(1, restaurantId)
            val resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) {
                return Restaurant(
                    id = resultSet.getObject("id", UUID::class.java),
                    name = resultSet.getString("name"),
                    address = resultSet.getString("address"),
                    category = resultSet.getString("category"),
                    cnpj = resultSet.getString("cnpj"),
                    items = findAllItems(resultSet.getObject("id", UUID::class.java)).toMutableSet()
                )
            } else {
                throw NoSuchElementException("Restaurant with ID $restaurantId not found")
            }
        } catch (e: SQLException) {
            throw RuntimeException("Error retrieving restaurant", e)
        } finally {
            connection.close()
        }
    }

    override fun findAll(): List<Restaurant> {
        val sql = "SELECT * FROM restaurant"
        val connection = DatabaseConfig.getConnection()
        val restaurants = mutableListOf<Restaurant>()
        try {
            val preparedStatement = connection.prepareStatement(sql)
            val resultSet = preparedStatement.executeQuery()
            while (resultSet.next()) {
                val orderId = resultSet.getObject("id", UUID::class.java)
                val name = resultSet.getString("name")
                val address = resultSet.getString("address")
                val category = resultSet.getString("category")
                val cnpj = resultSet.getString("cnpj")
                val items = findAllItems(resultSet.getObject("id", UUID::class.java))
                restaurants.add(
                    Restaurant(
                        id = orderId,
                        name = name,
                        address = address,
                        cnpj = cnpj,
                        category = category,
                        items = items.toMutableSet()
                )
                )
            }
        } catch (e: SQLException) {
            throw RuntimeException("Error retrieving all restaurants", e)
        } finally {
            connection.close()
        }
        return restaurants
    }

    override fun insertItem(restaurantId: UUID, restaurantItem: RestaurantItem) {
        val sql = "INSERT INTO restaurant_item (id, restaurant_id, name, price) VALUES (?, ?, ?, ?)"
        val connection = DatabaseConfig.getConnection()
        try {
            val preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setObject(1, restaurantItem.id)
            preparedStatement.setObject(2, restaurantId)
            preparedStatement.setString(3, restaurantItem.name)
            preparedStatement.setDouble(4, restaurantItem.price)
            preparedStatement.executeUpdate()
        } catch (e: SQLException) {
            throw RuntimeException("Error inserting item", e)
        } finally {
            connection.close()
        }
    }

    override fun findAllItems(restaurantId: UUID): List<RestaurantItem> {
        val sql = "SELECT * FROM restaurant_item WHERE restaurant_id = ?"
        val connection = DatabaseConfig.getConnection()
        val items = mutableListOf<RestaurantItem>()
        try {
            val preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setObject(1, restaurantId)
            val resultSet = preparedStatement.executeQuery()
            while (resultSet.next()) {
                items.add(RestaurantItem(
                    id = resultSet.getObject("id", UUID::class.java),
                    name = resultSet.getString("name"),
                    price = resultSet.getDouble("price")
                ))
            }
        } catch (e: SQLException) {
            throw RuntimeException("Error retrieving all items for restaurant", e)
        } finally {
            connection.close()
        }
        return items
    }

    override fun findOneItem(itemId: UUID): RestaurantItem {
        val sql = "SELECT * FROM restaurant_item WHERE id = ?"
        val connection = DatabaseConfig.getConnection()
        try {
            val preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setObject(1, itemId)
            val resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) {
                return RestaurantItem(
                    id = resultSet.getObject("id", UUID::class.java),
                    name = resultSet.getString("name"),
                    price = resultSet.getDouble("price")
                )
            } else {
                throw NoSuchElementException("Restaurant item with ID $itemId not found")
            }
        } catch (e: SQLException) {
            throw RuntimeException("Error retrieving restaurant item", e)
        } finally {
            connection.close()
        }
    }

}