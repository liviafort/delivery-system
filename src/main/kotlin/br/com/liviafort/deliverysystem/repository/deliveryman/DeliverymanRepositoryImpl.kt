package br.com.liviafort.deliverysystem.repository.deliveryman

import br.com.liviafort.deliverysystem.domain.deliveryman.Deliveryman
import br.com.liviafort.deliverysystem.domain.deliveryman.DeliverymanRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class DeliverymanRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
) : DeliverymanRepository {

    private val deliverymanRowMapper = RowMapper { rs, _ ->
        Deliveryman(
            id = rs.getObject("id", UUID::class.java),
            phone = rs.getString("phone"),
            name = rs.getString("name"),
            vehicle = rs.getString("vehicle")
        )
    }

    override fun save(deliveryman: Deliveryman) {
        val sql = "INSERT INTO deliveryman (id, phone, name, vehicle) VALUES (?, ?, ?, ?)"
        jdbcTemplate.update(sql, deliveryman.id, deliveryman.phone, deliveryman.name, deliveryman.vehicle)
    }

    override fun findOne(deliverymanId: UUID): Deliveryman {
        val sql = "SELECT id, phone, name, vehicle FROM deliveryman WHERE id = ?"
        return jdbcTemplate.queryForObject(sql, deliverymanRowMapper, deliverymanId)
            ?: throw NoSuchElementException("Deliveryman with ID $deliverymanId not found")
    }

    override fun findAll(): List<Deliveryman> {
        val sql = "SELECT * FROM deliveryman"
        return jdbcTemplate.query(sql, deliverymanRowMapper)
    }
}
