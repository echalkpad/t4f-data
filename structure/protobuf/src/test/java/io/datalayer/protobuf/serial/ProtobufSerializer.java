package io.datalayer.protobuf.serial;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Message;

public class ProtobufSerializer {
  private static final Logger LOGGER = LoggerFactory.getLogger(ProtobufSerializer.class);
  
  public static byte[] toBytes(Message message) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      message.writeTo(out);
      out.close();
    } catch (IOException e) {
      LOGGER.error("IOException while serializing Protobuf.", e);
      throw new RuntimeException("IOException while serializing Protobuf.", e);
    }
    return out.toByteArray();
  }

//  public static byte[] toCompressedBytes(Message message) throws DataException {
//    ByteArrayOutputStream out = new ByteArrayOutputStream();
//    try {
//      SnappyOutputStream sout = new SnappyOutputStream(out);
//      message.writeTo(sout);
//      sout.close();
//      out.close();
//    } catch (IOException e) {
//      LOGGER.error("IOException while serializing Protobuf.", e);
//      throw new DataException("IOException while serializing Protobuf.", e);
//    }
//    return out.toByteArray();
// }
//
}
