package com.slickbasics

case class Customer(id: Option[Int], name: String)

case class Product(id: Option[Int], name: String, price: Double)

case class Order(id: Option[Int],
                 customerId: Int,
                 productId: Int,
                 quantity: Int,
                 checkedOut: Boolean)
