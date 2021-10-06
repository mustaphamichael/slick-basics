package com.slickbasics

import slick.jdbc.{GetResult, JdbcBackend}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait Queries extends Schema {
  import profile.api._
  val db: JdbcBackend#DatabaseDef

  // place an order for customer, Michael M.(id = 1) buying 3 Google Pixel-4 phones (id = 1)
  def insertNewOrder(): Future[Int] = {
    val newOrder = Order(None, 1, 1, 3, checkedOut = false)
    val query = orders += newOrder
    db.run(query)
  }

  def insertMultipleOrder(): Future[Option[Int]] = {
    val newOrders = Seq(
      Order(None, 1, 2, 5, checkedOut = false), // Michael M. buying 5 Dish washers
      Order(None, 2, 3, 1, checkedOut = false), // Demo customer buying 1 Office chair
      Order(None, 1, 1, 1, checkedOut = true) // Michael M. bought and paid for 1 Google Pixel-4
    )
    db.run(orders ++= newOrders)
  }

  // get a list of orders yet to be paid for a single customer
  def fetchUnpaidOrders(customerId: Int): Future[Seq[Order]] = {
    val query =
      orders
        .filter(order => order.customerId === customerId && !order.checkedOut)
        .result
    db.run(query)
  }

  def updateProductPrice(productId: Int, newPrice: Double): Future[Int] = {
    val query =
      products.filter(_.id === productId).map(_.price).update(newPrice)
    db.run(query)
  }

  // fetch a detailed list of orders
  case class OrderReport(id: Int,
                         customer: String,
                         product: String,
                         quantity: Int,
                         totalPrice: Double,
                         checkedOut: Boolean)

  def generateOrderReport(): Future[Seq[OrderReport]] = {
    // a user-defined function for multiplying two numeric columns
    val multiply = SimpleBinaryOperator.apply[Double]("*")
    val query = for {
      order <- orders
      customer <- customers if customer.id === order.customerId
      product <- products if product.id === order.productId
    } yield
      (
        order.id,
        customer.name,
        product.name,
        order.quantity,
        multiply(order.quantity, product.price),
        order.checkedOut
      )
    db.run(query.result.map(_.map(OrderReport.tupled)))
  }

  def generateOrderReportInSQL(): Future[Vector[OrderReport]] = {
    implicit val orderResult: GetResult[OrderReport] = GetResult(
      r =>
        OrderReport(
          r.nextInt,
          r.nextString,
          r.nextString,
          r.nextInt,
          r.nextDouble,
          r.nextBoolean
      )
    )

    val query =
      sql"""SELECT ord.id, cus.name, pro.name, ord.quantity, ord.quantity * pro.price AS total_price, ord.checked_out
           FROM orders ord
           JOIN customers cus ON cus.id = ord.customer_id 
           JOIN products pro ON pro.id = ord.product_id""".as[OrderReport]
    db.run(query)
  }
}
