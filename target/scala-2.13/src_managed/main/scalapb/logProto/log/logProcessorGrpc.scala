package logProto.log

object logProcessorGrpc {
  val METHOD_GET_LOG: _root_.io.grpc.MethodDescriptor[logProto.log.sRequest, logProto.log.rResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("logProto.logProcessor", "GetLog"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[logProto.log.sRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[logProto.log.rResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(logProto.log.LogProto.javaDescriptor.getServices().get(0).getMethods().get(0)))
      .build()
  
  val SERVICE: _root_.io.grpc.ServiceDescriptor =
    _root_.io.grpc.ServiceDescriptor.newBuilder("logProto.logProcessor")
      .setSchemaDescriptor(new _root_.scalapb.grpc.ConcreteProtoFileDescriptorSupplier(logProto.log.LogProto.javaDescriptor))
      .addMethod(METHOD_GET_LOG)
      .build()
  
  trait logProcessor extends _root_.scalapb.grpc.AbstractService {
    override def serviceCompanion = logProcessor
    def getLog(request: logProto.log.sRequest): scala.concurrent.Future[logProto.log.rResponse]
  }
  
  object logProcessor extends _root_.scalapb.grpc.ServiceCompanion[logProcessor] {
    implicit def serviceCompanion: _root_.scalapb.grpc.ServiceCompanion[logProcessor] = this
    def javaDescriptor: _root_.com.google.protobuf.Descriptors.ServiceDescriptor = logProto.log.LogProto.javaDescriptor.getServices().get(0)
    def scalaDescriptor: _root_.scalapb.descriptors.ServiceDescriptor = logProto.log.LogProto.scalaDescriptor.services(0)
    def bindService(serviceImpl: logProcessor, executionContext: scala.concurrent.ExecutionContext): _root_.io.grpc.ServerServiceDefinition =
      _root_.io.grpc.ServerServiceDefinition.builder(SERVICE)
      .addMethod(
        METHOD_GET_LOG,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[logProto.log.sRequest, logProto.log.rResponse] {
          override def invoke(request: logProto.log.sRequest, observer: _root_.io.grpc.stub.StreamObserver[logProto.log.rResponse]): _root_.scala.Unit =
            serviceImpl.getLog(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .build()
  }
  
  trait logProcessorBlockingClient {
    def serviceCompanion = logProcessor
    def getLog(request: logProto.log.sRequest): logProto.log.rResponse
  }
  
  class logProcessorBlockingStub(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions = _root_.io.grpc.CallOptions.DEFAULT) extends _root_.io.grpc.stub.AbstractStub[logProcessorBlockingStub](channel, options) with logProcessorBlockingClient {
    override def getLog(request: logProto.log.sRequest): logProto.log.rResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_GET_LOG, options, request)
    }
    
    override def build(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions): logProcessorBlockingStub = new logProcessorBlockingStub(channel, options)
  }
  
  class logProcessorStub(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions = _root_.io.grpc.CallOptions.DEFAULT) extends _root_.io.grpc.stub.AbstractStub[logProcessorStub](channel, options) with logProcessor {
    override def getLog(request: logProto.log.sRequest): scala.concurrent.Future[logProto.log.rResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_GET_LOG, options, request)
    }
    
    override def build(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions): logProcessorStub = new logProcessorStub(channel, options)
  }
  
  def bindService(serviceImpl: logProcessor, executionContext: scala.concurrent.ExecutionContext): _root_.io.grpc.ServerServiceDefinition = logProcessor.bindService(serviceImpl, executionContext)
  
  def blockingStub(channel: _root_.io.grpc.Channel): logProcessorBlockingStub = new logProcessorBlockingStub(channel)
  
  def stub(channel: _root_.io.grpc.Channel): logProcessorStub = new logProcessorStub(channel)
  
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.ServiceDescriptor = logProto.log.LogProto.javaDescriptor.getServices().get(0)
  
}