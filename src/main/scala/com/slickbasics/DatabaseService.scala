package com.slickbasics

import com.typesafe.config.ConfigFactory
import org.flywaydb.core.Flyway

object DatabaseService {

  private val config = ConfigFactory.load()

  private val url = config.getString("database.url")
  private val user = config.getString("database.user")
  private val password = config.getString("database.password")
  private val driver = config.getString("database.driver")

  private val flyway = Flyway.configure().dataSource(url, user, password).load()

  val database =
    slick.jdbc.JdbcBackend.Database.forURL(url, user, password, driver = driver)

  def setUp(): Unit = {
    flyway.clean()
    flyway.migrate()
  }
}
