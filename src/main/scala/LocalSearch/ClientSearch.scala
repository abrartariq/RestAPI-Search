package LocalSearch

import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

import java.io.File
import java.time.LocalTime
import scala.io.Source
import scala.util.matching.Regex

object ClientSearch {

  //  Creating Logger
  val logger = LoggerFactory.getLogger(getClass)

    //Reading Values from Application Conf
  val cConfig = ConfigFactory.load("application.conf")

  val rPattern: String = cConfig.getString("evValues.rPattern")                               //Regex Pattern
  val deltaTime: LocalTime = LocalTime.parse(cConfig.getString("evValues.deltaTime"))         //Delta For Window
  val windowTime: LocalTime = LocalTime.parse(cConfig.getString("evValues.StartTime"))        //Start Time

  //Get list Current files present in the Log Directory
  def getListOfFiles(dir: String): List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }

  // Binary Search Code for Closest match
  def binarySearch(nameList: List[File], keytime: LocalTime): Int = {
    @scala.annotation.tailrec
    def gobinary(lo: Int, hi: Int): Int = {
      val mid = (lo + (hi - lo) / 2)
      if (lo > hi) {
        return mid
      }

      filenameToTime(nameList(mid)) match {
        case x if (x.compareTo(keytime) == 0) => mid
        case x if (x.compareTo(keytime) < 0) => gobinary(mid + 1, hi)
        case x if (x.compareTo(keytime) > 0) => gobinary(lo, mid - 1)
      }
    }

    gobinary(0, nameList.size - 1)
  }

  //Getting the first File that Satisfy the Constraint
  def stimeToIndex(nameList: List[File], keytime: LocalTime, closeIndex: Int): Int = {
    val closeTime: LocalTime = filenameToTime(nameList(closeIndex))

    if (closeIndex != 0 && (closeTime.compareTo(keytime) > 0)) {
      return closeIndex - 1
    }

    return closeIndex
  }

  // Geting the latest file that Satisfy the Constraint
  def etimeToIndex(nameList: List[File], keytime: LocalTime, closeIndex: Int): Int = {

    val closeTime: LocalTime = filenameToTime(nameList(closeIndex))

    if (closeIndex == 0 && (closeTime.compareTo(keytime) > 0)) {
      return -99
    } else if (closeIndex != 0 && (closeTime.compareTo(keytime) > 0)) {
      return closeIndex - 1
    }

    return closeIndex
  }

  //  Get the Range of files that satisfy the Constraint
  def getFileRange(startTime: LocalTime, endTime: LocalTime, nameList: List[File]): List[Int] = {

    // Checking the Time constraint
    val eResult: Int = binarySearch(nameList, endTime)
    val eRange: Int = etimeToIndex(nameList, endTime, eResult)

    if (eRange == -99) {
      return List[Int]()
    }

    val sResult: Int = binarySearch(nameList, startTime)
    val sRange: Int = stimeToIndex(nameList, startTime, sResult)

    return Range(sRange, eRange + 1).toList
  }
  // Extracting Time from File Name
  def filenameToTime(name: File): LocalTime = {
     return LocalTime.parse(name.getName.substring(0, 12))
  }

  //  Comparing TimeStamp Of Line KeyTime
  def lineTimeCompare(stringLine: String, keyTime: LocalTime): Int = {
    return LocalTime.parse(stringLine.split(" ")(0)).compareTo(keyTime)
  }

  //  Searchfiles Helper for Individual Files
  def searchFile(namePath: String, startTime: LocalTime, endTime: LocalTime): String = {
    val fileData = Source.fromFile(namePath)
    val fileDataList = fileData.getLines.toList
    fileData.close()

    if (lineTimeCompare(fileDataList.head, endTime) > 0 || lineTimeCompare(fileDataList.last, startTime) < 0) {
      return ""
    }
    val msgData: Regex = "[\" \"]-[\" \"].*".r
    var logData: String = ""
    for (elem <- fileDataList) {
      val lineData = elem.split(" ")
      if (lineTimeCompare(lineData(0), startTime) >= 0 && lineTimeCompare(lineData(0), endTime) <= 0) {
        logData = logData.concat(msgData.findFirstIn(elem).get.substring(3))
      }
    }
    return logData
  }

  //  Search all files via binary Search and find Closest Match
  def searchFiles(nameList: List[File], fileRange: List[Int], startTime: LocalTime, endTime: LocalTime): String = {

    var logData: String = ""
    fileRange.foreach { elem =>
      logData = logData.concat(searchFile(nameList(elem).getPath, startTime, endTime))
    }
    if (logData == "") {
      return "Error"
    } else {
      return logData
    }
  }

  // returns a 32-character MD5 hash version of the input string
  def md5HashPassword(usPassword: String): String = {
   def prependWithZeros(pwd: String): String = {
      "%1$32s".format(pwd).replace(' ', '0')
   }

    import java.math.BigInteger
    import java.security.MessageDigest
    val md = MessageDigest.getInstance("MD5")
    val digest: Array[Byte] = md.digest(usPassword.getBytes)
    val bigInt = new BigInteger(1, digest)
    val hashedPassword = bigInt.toString(16).trim
    prependWithZeros(hashedPassword)
  }

  //Entry Point
  def main(args: Array[String]): Unit = {
    logger.info("Login Begins")

    val fileList = getListOfFiles("logs/").sorted

    //Reading Values from Application Conf
    val cConfig = ConfigFactory.load("application.conf")
    val windowTime: LocalTime = LocalTime.parse(cConfig.getString("evValues.StartTime"))
    val rPattern: String = cConfig.getString("evValues.rPattern")
    val deltaTime: LocalTime = LocalTime.parse(cConfig.getString("evValues.deltaTime"))

    //Converting Time from Delta To START TIME AND END TIME
    val endTime: LocalTime = windowTime.plusHours(deltaTime.getHour()).plusMinutes(deltaTime.getMinute).plusSeconds(deltaTime.getSecond).plusNanos(deltaTime.getNano)
    val startTime: LocalTime = windowTime.minusHours(deltaTime.getHour()).minusMinutes(deltaTime.getMinute).minusSeconds(deltaTime.getSecond).minusNanos(deltaTime.getNano)

    logger.info("local Search With Parameters")
    logger.info("Start Time "+ startTime)
    logger.info("End Time "+ endTime)
    logger.info("Pattern "+ rPattern)
    logger.info("Optional FileName "+ "")

    // Local Logging of File Finding for Test Implementation
    val fileRange: List[Int] = getFileRange(startTime, endTime, fileList)
    val result: String = searchFiles(fileList,fileRange, startTime, endTime)
    logger.info(s"Hash of Message = " + " " + md5HashPassword(result))
  }
}

