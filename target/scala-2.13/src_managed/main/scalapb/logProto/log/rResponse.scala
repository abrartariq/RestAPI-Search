// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package logProto.log

@SerialVersionUID(0L)
final case class rResponse(
    statusCode: _root_.scala.Int = 0,
    responseVal: _root_.scala.Predef.String = "",
    unknownFields: _root_.scalapb.UnknownFieldSet = _root_.scalapb.UnknownFieldSet.empty
    ) extends scalapb.GeneratedMessage with scalapb.lenses.Updatable[rResponse] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = statusCode
        if (__value != 0) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeInt32Size(1, __value)
        }
      };
      
      {
        val __value = responseVal
        if (!__value.isEmpty) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, __value)
        }
      };
      __size += unknownFields.serializedSize
      __size
    }
    override def serializedSize: _root_.scala.Int = {
      var read = __serializedSizeCachedValue
      if (read == 0) {
        read = __computeSerializedValue()
        __serializedSizeCachedValue = read
      }
      read
    }
    def writeTo(`_output__`: _root_.com.google.protobuf.CodedOutputStream): _root_.scala.Unit = {
      {
        val __v = statusCode
        if (__v != 0) {
          _output__.writeInt32(1, __v)
        }
      };
      {
        val __v = responseVal
        if (!__v.isEmpty) {
          _output__.writeString(2, __v)
        }
      };
      unknownFields.writeTo(_output__)
    }
    def withStatusCode(__v: _root_.scala.Int): rResponse = copy(statusCode = __v)
    def withResponseVal(__v: _root_.scala.Predef.String): rResponse = copy(responseVal = __v)
    def withUnknownFields(__v: _root_.scalapb.UnknownFieldSet) = copy(unknownFields = __v)
    def discardUnknownFields = copy(unknownFields = _root_.scalapb.UnknownFieldSet.empty)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = statusCode
          if (__t != 0) __t else null
        }
        case 2 => {
          val __t = responseVal
          if (__t != "") __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PInt(statusCode)
        case 2 => _root_.scalapb.descriptors.PString(responseVal)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = logProto.log.rResponse
    // @@protoc_insertion_point(GeneratedMessage[logProto.rResponse])
}

object rResponse extends scalapb.GeneratedMessageCompanion[logProto.log.rResponse] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[logProto.log.rResponse] = this
  def parseFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): logProto.log.rResponse = {
    var __statusCode: _root_.scala.Int = 0
    var __responseVal: _root_.scala.Predef.String = ""
    var `_unknownFields__`: _root_.scalapb.UnknownFieldSet.Builder = null
    var _done__ = false
    while (!_done__) {
      val _tag__ = _input__.readTag()
      _tag__ match {
        case 0 => _done__ = true
        case 8 =>
          __statusCode = _input__.readInt32()
        case 18 =>
          __responseVal = _input__.readStringRequireUtf8()
        case tag =>
          if (_unknownFields__ == null) {
            _unknownFields__ = new _root_.scalapb.UnknownFieldSet.Builder()
          }
          _unknownFields__.parseField(tag, _input__)
      }
    }
    logProto.log.rResponse(
        statusCode = __statusCode,
        responseVal = __responseVal,
        unknownFields = if (_unknownFields__ == null) _root_.scalapb.UnknownFieldSet.empty else _unknownFields__.result()
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[logProto.log.rResponse] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage eq scalaDescriptor), "FieldDescriptor does not match message type.")
      logProto.log.rResponse(
        statusCode = __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Int]).getOrElse(0),
        responseVal = __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Predef.String]).getOrElse("")
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = LogProto.javaDescriptor.getMessageTypes().get(1)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = LogProto.scalaDescriptor.messages(1)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = logProto.log.rResponse(
    statusCode = 0,
    responseVal = ""
  )
  implicit class rResponseLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, logProto.log.rResponse]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, logProto.log.rResponse](_l) {
    def statusCode: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Int] = field(_.statusCode)((c_, f_) => c_.copy(statusCode = f_))
    def responseVal: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.responseVal)((c_, f_) => c_.copy(responseVal = f_))
  }
  final val STATUSCODE_FIELD_NUMBER = 1
  final val RESPONSEVAL_FIELD_NUMBER = 2
  def of(
    statusCode: _root_.scala.Int,
    responseVal: _root_.scala.Predef.String
  ): _root_.logProto.log.rResponse = _root_.logProto.log.rResponse(
    statusCode,
    responseVal
  )
  // @@protoc_insertion_point(GeneratedMessageCompanion[logProto.rResponse])
}