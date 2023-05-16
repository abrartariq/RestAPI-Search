package test

import LocalSearch.ClientSearch

import java.io.File
import java.time.LocalTime
import org.scalatest.funsuite.AnyFunSuite

class ClientSearchTest extends AnyFunSuite {

  test("getListOfFiles should return a list of files") {
    val dir = "logs/"
    val files = ClientSearch.getListOfFiles(dir).sorted
    assert(files.nonEmpty && files.forall(_.isFile))
  }


  test("getListOfFiles should return a list of 17 files") {
    val dir = "logs/"
    val files = ClientSearch.getListOfFiles(dir).sorted
    assert(files.length == 17)
  }

  test("binarySearch should return an index of the closest file to the given time => First Index => 0") {
    val dir = "logs/"
    val files = ClientSearch.getListOfFiles(dir).sorted
    val keyTime = LocalTime.parse("03:10:00.000")
    val index = ClientSearch.binarySearch(files, keyTime)
    assert(index == 0)
  }

  test("getFileRange should return a list of indices representing the range of files satisfying the constraint") {
    val dir = "logs/"
    val files = ClientSearch.getListOfFiles(dir).sorted
    val startTime = LocalTime.parse("03:12:00.000")
    val endTime = LocalTime.parse("03:22:00.000")
    val fileRange = ClientSearch.getFileRange(startTime, endTime, files)
    assert(fileRange == List(0, 1))

  }

  test("filenameToTime should return the LocalTime object representing the time extracted from the file name") {
    val file = new File("logs/03:31:29.179_2022-10-30.LogFileGenerator.2022-10-30.log")
    val extractedTime = ClientSearch.filenameToTime(file)

    assert(extractedTime.equals(LocalTime.parse("03:31:29.179")))
  }

  test("md5HashPassword should return the MD5 hash of the input string") {
      val input = "Hello, world!"
      val hashedPassword = ClientSearch.md5HashPassword(input)

      assert(hashedPassword == "6cd3556deb0da54bca060b4c39479839")
    }
}
