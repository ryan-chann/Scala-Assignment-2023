package edu.sunway.oopassignment

import edu.sunway.oopassignment.model.{Country, GdpData}

import java.io.FileNotFoundException
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source

object MainApp {

  def main(args: Array[String]) = {
    try {
      val fileName = getClass.getResource("csv/" + "gdpdata.csv").toString.replace("/", "\\\\").substring(7)
      val bufferedSource = Source.fromFile(fileName)
      var index = 0
      var largest = 0.0
      var highestGDPCountry = ""
      var countryListBuffer = ListBuffer[Country]()
      for (row <- bufferedSource.getLines()) {
        index += 1

        if (index >= 3) {
          val dataArray = row.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)")
          var gdpDataObject = new GdpData(0, "", 0, "", 0.0)

          for (token <- dataArray) {
            gdpDataObject = new GdpData(dataArray(0).toInt, dataArray(1).replace("\"", ""), dataArray(2).toInt, dataArray(3), dataArray(4).replace("\"", "").replace(",", "").toDouble)
          }

          if (gdpDataObject.series.equals("GDP per capita (US dollars)")) {

            if (countryListBuffer.isEmpty) {
              countryListBuffer += new Country(gdpDataObject.areaName, Map(gdpDataObject.year -> gdpDataObject.value))
            }
            else {
              if (countryListBuffer.exists(country => country.name.equalsIgnoreCase(gdpDataObject.areaName))) {

                var countryToUpdate = countryListBuffer.find(_.name.equalsIgnoreCase(gdpDataObject.areaName))

                countryToUpdate match {
                  case Some(country) => {
                    val updatedGdpByYear = country.gdpByYear + (gdpDataObject.year -> gdpDataObject.value)
                    country.gdpByYear = updatedGdpByYear
                  }
                  case None => println("Country not found")
                }
              }
              else {
                countryListBuffer += new Country(gdpDataObject.areaName, Map(gdpDataObject.year -> gdpDataObject.value))
              }
            }

            if (gdpDataObject.value >= largest) {
              largest = gdpDataObject.value
              highestGDPCountry = gdpDataObject.areaName
            }
          }
        }
      }

      printf("Which country has the highest GDP per capital (US dollars): %s (GDP per capital USD - %d)\n\n", highestGDPCountry, largest.toInt)

      for (countries <- countryListBuffer) {
        if (countries.name.equalsIgnoreCase("Malaysia")) {
          printf("What is the average GDP per capital(US dollars) for Malaysia in the provided data: %.2f\n\n", countries.gdpByYear.values.sum / countries.gdpByYear.size)
        }
      }

      val lowestAvgGdpCountries = countryListBuffer.sortBy(country => country.gdpByYear.values.sum / country.gdpByYear.size).take(5)

      println("Which of the 5 countries is the lowest average GDP per capital(US dollars) in the provided data?")
      var foreachIndex = 0
      for (lowestCountries <- lowestAvgGdpCountries) {
        foreachIndex += 1
        printf("%d. %s (Average GDP per capital: %.2f)\n", foreachIndex, lowestCountries.name, lowestCountries.gdpByYear.values.sum / lowestCountries.gdpByYear.size)
      }

      bufferedSource.close()
    }
    catch {
      case fnfe: FileNotFoundException => fnfe.printStackTrace()
      case npe: NullPointerException => npe.printStackTrace()
    }
  }
}

