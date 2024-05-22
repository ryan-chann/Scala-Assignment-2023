package edu.sunway.oopassignment.model

case class GdpData (
  var areaCode: Int,
  var areaName: String,
  var year: Int,
  var series: String,
  var value: Double
)