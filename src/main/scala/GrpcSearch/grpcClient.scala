package GrpcSearch

import com.typesafe.config.ConfigFactory
import io.grpc.{ManagedChannel, ManagedChannelBuilder, StatusRuntimeException}
import logProto.log.logProcessorGrpc.logProcessorBlockingStub
import logProto.log.{logProcessorGrpc, sRequest}
import org.slf4j.LoggerFactory

import java.util.concurrent.TimeUnit

// Grpc Client Class
class grpcClient private(val channel: ManagedChannel, val blockingStub: logProcessorBlockingStub) {


  val logger = LoggerFactory.getLogger(getClass)

  // Graceful termination of Client
  def graceTerminate(): Unit = channel.shutdown.awaitTermination(ConfigFactory.load().getLong("evValues.timeout"), TimeUnit.SECONDS)

  def doRequest(sTime:String ,dTime: String, rPattern: String):Unit ={
    logger.info("Creating Request")
    val request: sRequest = sRequest(sTime,dTime,rPattern)
    logger.info("Sending Request")
    logger.info("Start Time "+ sTime)
    logger.info("Delta Time "+ dTime)
    logger.info("Pattern "+ rPattern)

    try {
      val response = blockingStub.getLog(request)
      logger.info(s"ResponseCode Client : ${response.statusCode}")
      logger.info(s"Response Client: ${response.responseVal}")
    }
    catch {
      case exp: StatusRuntimeException =>
        logger.warn(s"Rpc Unsuccessful Code: ,${exp.getStatus}")
    }
  }
}


// GrpcClient Instance
object grpcClient {
  //  Creating Logger
  val logger = LoggerFactory.getLogger(getClass)

  //Reading Values from Application Conf
  val cConfig = ConfigFactory.load("application.conf")

  val rPattern: String = cConfig.getString("evValues.rPattern") //Regex Pattern
  val deltaTime: String = cConfig.getString("evValues.deltaTime") //Delta For Window
  val windowTime: String = cConfig.getString("evValues.StartTime") //Start Time
  val host: String = cConfig.getString("evValues.Host") //Host
  val port: Int = cConfig.getInt("evValues.Port") //Port

  // Applying Stub
  def apply(): grpcClient = {
    val channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build
    val bStub = logProcessorGrpc.blockingStub(channel)
    new grpcClient(channel, bStub)
  }

  // Entry Point
  def main(args: Array[String]): Unit = {
    logger.info(s"Starting Client and connecting at $host:$port")
    val gClient = grpcClient()

    try {
      gClient.doRequest(windowTime,deltaTime,rPattern)
    }catch {
      case _: Throwable =>
    }
    finally {
      gClient.graceTerminate()
    }
  }
}


