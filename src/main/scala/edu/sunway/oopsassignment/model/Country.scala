package edu.sunway.oopassignment.model


case class Country (
  var name: String,
  var gdpByYear: Map[Int, Double]
)