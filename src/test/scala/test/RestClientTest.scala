package test

import RestApi.RestClient

import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import com.typesafe.config.ConfigFactory
import org.scalatest.funsuite.AnyFunSuite
import java.time.LocalTime

class RestClientTest extends AnyFunSuite {

  test("Data Read Should Unchanged Equal To Assign Values"){
    val cConfig = ConfigFactory.load("application.conf")

    val expectedAwsUrl = "https://4waphqtr8c.execute-api.us-east-2.amazonaws.com/test/files"
    val expectedRPattern = "(.*?)"
    val expectedDeltaTime = LocalTime.parse("00:02:00.000")
    val expectedWindowTime = LocalTime.parse("03:13:45.298")

    val awsUrl = cConfig.getString("evValues.AwsURL")
    val rPattern = cConfig.getString("evValues.rPattern")
    val deltaTime = LocalTime.parse(cConfig.getString("evValues.deltaTime"))
    val windowTime = LocalTime.parse(cConfig.getString("evValues.StartTime"))

    assert(awsUrl == expectedAwsUrl, s"Expected AWS URL: $expectedAwsUrl, but got: $awsUrl")
    assert(rPattern == expectedRPattern, s"Expected regex pattern: $expectedRPattern, but got: $rPattern")
    assert(deltaTime == expectedDeltaTime, s"Expected delta time: $expectedDeltaTime, but got: $deltaTime")
    assert(windowTime == expectedWindowTime, s"Expected window time: $expectedWindowTime, but got: $windowTime")
  }



  test("getEndTime should return the end time of the window based on the start time and delta time") {
    val startTime = LocalTime.parse("03:15:00.000")
    val deltaTime = LocalTime.parse("00:02:00.000")
    val endTime = RestClient.getEndTime(startTime, deltaTime)

    assert(endTime == LocalTime.parse("03:17:00.000"))
  }

  test("getStartTime should return the start time of the window based on the end time and delta time") {
    val endTime = LocalTime.parse("03:15:00.000")
    val deltaTime = LocalTime.parse("00:02:00.000")
    val startTime = RestClient.getStartTime(endTime, deltaTime)

    assert(startTime == LocalTime.parse("03:13:00.000"))
  }

  test("createRequest should return an HttpRequest with the correct parameters") {
    val aUrl = "https://example.com/test"
    val sTime = "03:13:00.000"
    val eTime = "03:15:00.000"
    val rPattern = "(.*?)"
    val fileName = "03:27:22.451_2022-10-30.LogFileGenerator.2022-10-30.log"
    val request = RestClient.createRequest(aUrl, sTime, eTime, rPattern, fileName)

    assert(request.isInstanceOf[HttpRequest])
    assert(request.method == HttpMethods.GET)
    assert(request.uri.toString == s"https://example.com/test?sTime=$sTime&eTime=$eTime&rPattern=$rPattern&fileName=$fileName")
  }
}