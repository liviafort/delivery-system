package br.com.liviafort.deliverysystem.repository.customer

import br.com.liviafort.deliverysystem.domain.customer.Customer
import br.com.liviafort.deliverysystem.domain.customer.CustomerRepository
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.util.UUID

@Primary
@Repository
class CustomerRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
) : CustomerRepository {

    private val customerRowMapper = RowMapper { rs, _ ->
        Customer(
            id = rs.getObject("id", UUID::class.java),
            phone = rs.getString("phone"),
            name = rs.getString("name"),
            address = rs.getString("address")
        )
    }

    override fun save(customer: Customer) {
        val sql = "INSERT INTO customer (id, phone, name, address) VALUES (?, ?, ?, ?)"
        jdbcTemplate.update(sql, customer.id, customer.phone, customer.name, customer.address)
    }

    override fun findAll(): List<Customer> {
        val sql = "SELECT * FROM customer"
        return jdbcTemplate.query(sql, customerRowMapper)
    }

    override fun findOne(customerId: UUID): Customer {
        val sql = "SELECT * FROM customer WHERE id = ?"
        return jdbcTemplate.queryForObject(sql, customerRowMapper, customerId)
            ?: throw NoSuchElementException("Customer with ID $customerId not found")
    }
}
