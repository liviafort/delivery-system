package br.com.liviafort.deliverysystem.repository.deliveryman

import br.com.liviafort.deliverysystem.config.DatabaseConfig
import br.com.liviafort.deliverysystem.domain.deliveryman.Deliveryman
import br.com.liviafort.deliverysystem.domain.deliveryman.DeliverymanRepository
import java.sql.SQLException
import java.util.*

class DeliverymanRepositoryInMemory: DeliverymanRepository {

    override fun save(deliveryman: Deliveryman) {
        val sql = "INSERT INTO deliveryman (id, phone, name, vehicle) VALUES (?, ?, ?, ?)"
        val connection = DatabaseConfig.getConnection()
        try {
            val statement = connection.prepareStatement(sql)
            statement.setObject(1, deliveryman.id)
            statement.setString(2, deliveryman.phone)
            statement.setString(3, deliveryman.name)
            statement.setString(4, deliveryman.vehicle)
            statement.executeUpdate()
        } catch (e: SQLException) {
            throw RuntimeException("Error saving deliveryman", e)
        } finally {
            connection.close()
        }
    }

    override fun findOne(deliverymanId: UUID): Deliveryman {
        val sql = "SELECT id, phone, name, vehicle FROM deliveryman WHERE id = ?"
        val connection = DatabaseConfig.getConnection()
        try {
            val preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setObject(1, deliverymanId)
            val resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) {
                return Deliveryman(
                    id = resultSet.getObject("id", UUID::class.java),
                    phone = resultSet.getString("phone"),
                    name = resultSet.getString("name"),
                    vehicle = resultSet.getString("vehicle")
                )
            } else {
                throw NoSuchElementException("Deliveryman with ID $deliverymanId not found")
            }
        } catch (e: SQLException) {
            throw RuntimeException("Error retrieving deliveryman", e)
        } finally {
            connection.close()
        }
    }

    override fun findAll(): List<Deliveryman> {
        val sql = "SELECT * FROM deliveryman"
        val connection = DatabaseConfig.getConnection()
        val deliverymen = mutableListOf<Deliveryman>()
        try {
            val preparedStatement = connection.prepareStatement(sql)
            val resultSet = preparedStatement.executeQuery()
            while (resultSet.next()) {
                val deliveryman = Deliveryman(
                    id = resultSet.getObject("id", UUID::class.java),
                    phone = resultSet.getString("phone"),
                    name = resultSet.getString("name"),
                    vehicle = resultSet.getString("vehicle")
                )
                deliverymen.add(deliveryman)
            }
        } catch (e: SQLException) {
            throw RuntimeException("Error retrieving deliverymen", e)
        } finally {
            connection.close()
        }
        return deliverymen
    }

}