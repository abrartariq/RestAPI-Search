package RestApi

import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import sttp.client3.{HttpURLConnectionBackend, UriContext, basicRequest}
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpEntity, HttpMethods, HttpRequest, HttpResponse, StatusCodes}
import akka.stream.ActorMaterializer

import java.time.LocalTime
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}



object RestClient {


  implicit val system: ActorSystem = ActorSystem() // Akka actors
  implicit val materialzer: ActorMaterializer = ActorMaterializer()
  import system.dispatcher

  // Creating Logger
  val logger = LoggerFactory.getLogger(getClass)

  //Reading Values from Application Conf
  val cConfig = ConfigFactory.load("application.conf")

  val awsUrl: String = cConfig.getString("evValues.AwsURL")                                   //Gateway Url
  val rPattern: String = cConfig.getString("evValues.rPattern")                               //Regex Pattern
  val deltaTime: LocalTime = LocalTime.parse(cConfig.getString("evValues.deltaTime"))         //Delta For Window
  val windowTime: LocalTime = LocalTime.parse(cConfig.getString("evValues.StartTime"))        //Start Time


  //  Graceful Termination For Akka System
  def graceTerminate(): Unit = Http().shutdownAllConnectionPools().onComplete(_ => system.terminate())

  //  Creating HttpRequest
  def createRequest(aUrl: String, sTime: String, eTime: String, rPattern: String, fileName: String): HttpRequest ={
    val fullUrl: String = s"$aUrl?sTime=$sTime&eTime=$eTime&rPattern=$rPattern&fileName=$fileName"

    val cRequest: HttpRequest = HttpRequest(
      method = HttpMethods.GET,
      uri = fullUrl,
      entity = HttpEntity.Empty,
    )
    cRequest
  }

  //  Doing HttpRequest Using Akka with Async Futures
  def akkaRequest(aUrl: String, sTime: String, eTime: String, rPattern: String, fileName: String)={
    val responseFuture: Future[HttpResponse] = Http().singleRequest(createRequest(aUrl, sTime, eTime, rPattern, fileName))


    logger.info("Getting Response")
    val responseFutureResult = Await.ready(responseFuture, Duration.Inf).value.get
    //    responseFutureResult.onComplete {
    responseFutureResult match {
      case Success(res) =>
        logger.info(s"Status code: ${res.status}")
        if (res.status != StatusCodes.OK){
          logger.warn(s"Status Code ${res.status}")
          graceTerminate()
        }else{
          res.entity.toStrict(2.seconds).map(_.data.utf8String).onComplete({
            case Success(content) =>
              logger.info(s"Response Body: $content")
              graceTerminate()
            case Failure(_) =>
              logger.warn("Status OK, Content Corrupt")
              graceTerminate()
          })
        }
      case Failure(exception) =>
        logger.error(s"Request failed: $exception")
        graceTerminate()
    }
  }

  //  Using sttp3 for Get Request RestFul
  def sttpRequest(aURL: String,sTime: String, eTime: String, rPattern: String, fileName: String): (Int, String) = {
    val logger = LoggerFactory.getLogger(getClass)
    val fullUrl: String = s"$aURL?sTime=$sTime&eTime=$eTime&rPattern=$rPattern&fileName=$fileName"

    val request = basicRequest.get(uri"$fullUrl")
    logger.info(fullUrl)
    val backend = HttpURLConnectionBackend()
    val response = request.send(backend)

    logger.info("Getting Response Sttp")
    logger.info(s"Message Code ${response.code}")
    logger.info(s"${response.body}")
    (response.code.toString().toInt, response.body.toString)
  }

    //Converting Delta Time To EndTime
  def getEndTime(windowTime: LocalTime, deltaTime: LocalTime): LocalTime = windowTime.plusHours(deltaTime.getHour()).plusMinutes(deltaTime.getMinute).plusSeconds(deltaTime.getSecond).plusNanos(deltaTime.getNano)

  //Converting Delta Time To StartTime
  def getStartTime(windowTime: LocalTime, deltaTime: LocalTime): LocalTime = windowTime.minusHours(deltaTime.getHour()).minusMinutes(deltaTime.getMinute).minusSeconds(deltaTime.getSecond).minusNanos(deltaTime.getNano)

  //Entry Point
  def main(args: Array[String]): Unit = {
    val logger = LoggerFactory.getLogger(getClass)
    logger.info("Logging Begins")


    // Converting Time from Delta-Time to START-TIME and END-TIME
    val endTime: LocalTime = getEndTime(windowTime,deltaTime)
    val startTime: LocalTime = getStartTime(windowTime,deltaTime)

    logger.info("Sending Request With Parameters")
    logger.info("Start Time "+ startTime)
    logger.info("End Time "+ endTime)
    logger.info("Pattern "+ rPattern)
    logger.info("Optional FileName "+ "")


    // Doing Akka Request To lambda Function
    akkaRequest(awsUrl, startTime.toString, endTime.toString, rPattern, "")

    // Doing Sttp3 Request To lambda Function
    // sttpRequest(awsUrl,startTime.toString, endTime.toString, rPattern, "")

    logger.info("Logging Ends")
  }
}

