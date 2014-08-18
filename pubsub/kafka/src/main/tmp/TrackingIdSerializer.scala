package aos.kafka;

import kafka.message.Message
import kafka.serializer.Encoder

class TrackingDataSerializer extends Encoder[TrackingData] {

  // Say you want to use your own custom Avro encoding
  CustomAvroEncoder avroEncoder = new CustomAvroEncoder();

  def toMessage(event: TrackingData): Message = {
    new Message(avroEncoder.getBytes(event));
  }

}
