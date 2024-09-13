package br.com.liviafort.deliverysystem.repository.customer

import br.com.liviafort.deliverysystem.config.DatabaseConfig
import br.com.liviafort.deliverysystem.domain.customer.Customer
import br.com.liviafort.deliverysystem.domain.customer.CustomerRepository
import java.sql.SQLException
import java.sql.Statement
import java.util.UUID

class CustomerRepositoryInMemory: CustomerRepository {

    override fun save(customer: Customer) {
        val sql = "INSERT INTO customer (id, phone, name, address) VALUES (?, ?, ?, ?)"
        val connection = DatabaseConfig.getConnection()
        try {
            val statement = connection.prepareStatement(sql)
            statement.setObject(1, customer.id)
            statement.setString(2, customer.phone)
            statement.setString(3, customer.name)
            statement.setString(4, customer.address)
            statement.executeUpdate()
        } catch (e: SQLException) {
            throw RuntimeException("Error saving customer", e)
        } finally {
            connection.close()
        }
    }

    override fun findAll(): List<Customer> {
        val sql = "SELECT * FROM customer"
        val connection = DatabaseConfig.getConnection()
        val customers = mutableListOf<Customer>()
        try {
            val preparedStatement = connection.prepareStatement(sql)
            val resultSet = preparedStatement.executeQuery()
            while (resultSet.next()) {
                val customer = Customer(
                    id = resultSet.getObject("id", UUID::class.java),
                    phone = resultSet.getString("phone"),
                    name = resultSet.getString("name"),
                    address = resultSet.getString("address")
                )
                customers.add(customer)
            }
        } catch (e: SQLException) {
            throw RuntimeException("Error retrieving customers", e)
        } finally {
            connection.close()
        }
        return customers
    }

    override fun findOne(customerId: UUID): Customer {
        val sql = "SELECT * FROM customer WHERE id = ?"
        val connection = DatabaseConfig.getConnection()
        try {
            val preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setObject(1, customerId)
            val resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) {
                return Customer(
                    id = resultSet.getObject("id", UUID::class.java),
                    name = resultSet.getString("name"),
                    phone = resultSet.getString("phone"),
                    address = resultSet.getString("address")
                )
            } else {
                throw NoSuchElementException("Customer with ID $customerId not found")
            }
        } catch (e: SQLException) {
            throw RuntimeException("Error retrieving customer", e)
        } finally {
            connection.close()
        }
    }
}