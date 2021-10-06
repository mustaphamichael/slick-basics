package com.slickbasics

import slick.jdbc.PostgresProfile

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

object Main extends App with Queries {
  val dbService = DatabaseService
  dbService.setUp()

  override val db = dbService.database
  override val profile = PostgresProfile

  try {
    val tests = for {
      _ <- insertNewOrder()
      _ <- insertMultipleOrder()
      _ <- fetchUnpaidOrders(1) // for customer(id = 1)
      report <- generateOrderReport()
      reportSQL <- generateOrderReportInSQL()
      _ <- updateProductPrice(2, 200.0) // update the price of the dish washer(id = 2) to 200.0
    } yield (report, reportSQL)

    val t = Await.result(tests, 1.minute)
    println(t)
  } finally {
    db.close()
  }
}
