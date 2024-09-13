package br.com.liviafort.deliverysystem.config

import java.sql.Connection
import java.sql.DriverManager

object DatabaseConfig {
    private const val URL = "jdbc:postgresql://localhost:5433/postgres"
    private const val USER = "postgres"
    private const val PASSWORD = "1234"

    fun getConnection(): Connection {
        return DriverManager.getConnection(URL, USER, PASSWORD)
    }
}