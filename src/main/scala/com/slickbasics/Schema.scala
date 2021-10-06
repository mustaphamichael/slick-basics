package com.slickbasics

trait Schema {
  val profile: slick.jdbc.JdbcProfile

  import profile.api._

  class Customers(tag: Tag) extends Table[Customer](tag, "customers") {
    def id = column[Int]("id", O.AutoInc)
    def name = column[String]("name")

    def * = (id.?, name) <> (Customer.tupled, Customer.unapply)
  }
  lazy val customers = TableQuery[Customers]

  class Products(tag: Tag) extends Table[Product](tag, "products") {
    def id = column[Int]("id", O.AutoInc)
    def name = column[String]("name")
    def price = column[Double]("price")

    def * = (id.?, name, price) <> (Product.tupled, Product.unapply)
  }
  lazy val products = TableQuery[Products]

  class Orders(tag: Tag) extends Table[Order](tag, "orders") {
    def id = column[Int]("id", O.AutoInc)
    def customerId = column[Int]("customer_id")
    def productId = column[Int]("product_id")
    def quantity = column[Int]("quantity")
    def checkedOut = column[Boolean]("checked_out")

    def * =
      (id.?, customerId, productId, quantity, checkedOut) <> (Order.tupled, Order.unapply)
  }
  lazy val orders = TableQuery[Orders]
}
