package io.datalayer.protobuf;

import static org.junit.Assert.assertEquals;
import io.datalayer.protobuf.builder.Person1;
import io.datalayer.protobuf.builder.Person2;
import io.datalayer.protobuf.serial.ProtobufSerializer;

import org.junit.Test;

import com.google.protobuf.InvalidProtocolBufferException;

public class SchemaEvolutionTest {
    
    @Test
    public void test() throws InvalidProtocolBufferException  {

        Person1.Person.Builder builder = Person1.Person.newBuilder();
        builder.setUserName("a username");
        
        byte[] person1 = ProtobufSerializer.toBytes(builder.build());
        
        Person2.Person buf = null;
        buf = Person2.Person.parseFrom(person1);
        
        assertEquals(builder.getUserName(), buf.getUserName());
        
    }

}
