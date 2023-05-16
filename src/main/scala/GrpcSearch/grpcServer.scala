package GrpcSearch

import RestApi.RestClient._

import scala.concurrent.{ExecutionContext, Future}
import com.typesafe.config.ConfigFactory
import io.grpc.{Server, ServerBuilder}
import logProto.log.{logProcessorGrpc, rResponse, sRequest}
import logProto.log.logProcessorGrpc.logProcessor
import org.slf4j.LoggerFactory

import java.time.LocalTime

//Class For grpcServer
class grpcServer (executionContext: ExecutionContext ){self =>
  val logger = LoggerFactory.getLogger(getClass)
  var server: Server = null

  val cConfig = ConfigFactory.load("application.conf")

  val awsUrl: String = cConfig.getString("evValues.AwsURL")                                   //Gateway Url
  val host: String = cConfig.getString("evValues.Host")                                       //Host
  val port: Int = cConfig.getInt("evValues.Port")                                             //Port


  //Start Function
  def start(): Unit ={
    server = ServerBuilder.forPort(port).addService(logProcessorGrpc.bindService(new logProcessorImpl, executionContext)).build.start
  }
  sys.addShutdownHook{
    logger.warn("Terminating Server")
    if(server != null) server.shutdown()
  }
  //Clean ShutDown for Server
  def gracefulShutdown(): Unit = if (server != null) server.awaitTermination()

  //Parsing Time from String to Local Time
  def convertToLocalTime(valTime: String): LocalTime = LocalTime.parse(valTime)

  //Converting Delta Time To EndTime
  def getEndTime(windowTime: LocalTime, deltaTime: LocalTime): LocalTime = windowTime.plusHours(deltaTime.getHour()).plusMinutes(deltaTime.getMinute).plusSeconds(deltaTime.getSecond).plusNanos(deltaTime.getNano)

  //Converting Delta Time To StartTime
  def getStartTime(windowTime: LocalTime, deltaTime: LocalTime): LocalTime = windowTime.minusHours(deltaTime.getHour()).minusMinutes(deltaTime.getMinute).minusSeconds(deltaTime.getSecond).minusNanos(deltaTime.getNano)

  //Main Service Class For GRPC
  private class logProcessorImpl extends logProcessorGrpc.logProcessor{
    override def getLog(req: sRequest): Future[rResponse] = {
    // Converting Time from Delta-Time to START-TIME and END-TIME
      val startTime = getStartTime(convertToLocalTime(req.startTime), convertToLocalTime(req.deltaTime))
      val endTime = getEndTime(convertToLocalTime(req.startTime), convertToLocalTime(req.deltaTime))
      val rPattern = req.rPattern

      logger.info("Sending Request With Parameters")
      logger.info("Start Time "+ startTime)
      logger.info("End Time "+ endTime)
      logger.info("Pattern "+ rPattern)

      val result = sttpRequest(awsUrl,startTime.toString, endTime.toString, rPattern, "")
      val responseToSend = rResponse(result._1, result._2)

      logger.info("Sening Response Back to Client")

      Future.successful(responseToSend)

    }
  }
}


object grpcServer {
  val logger = LoggerFactory.getLogger(getClass)

  // Entry Point
  def main(args: Array[String]): Unit = {

    logger.info("Starting Server at 127.0.0.1:55551")
    val server = new grpcServer(ExecutionContext.global)
    server.start()
    server.gracefulShutdown()
  }
}

