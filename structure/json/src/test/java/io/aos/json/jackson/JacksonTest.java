/****************************************************************
 * Licensed to the AOS Community (AOS) under one or more        *
 * contributor license agreements.  See the NOTICE file         *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The AOS licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/
package io.aos.json.jackson;

import io.aos.json.jackson.model.User;
import io.aos.json.jackson.model.User.Gender;
import io.aos.json.jackson.model.User.Name;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Lists;

public class JacksonTest {

    @Test
    public void testTyped() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        User user = mapper.readValue(new File("./src/test/resources/user.json"), User.class);
        System.out.println(user.getGender());
        System.out.println(user.getTypes().size());
        user.setGender(Gender.FEMALE);
        mapper.writeValue(new File("./target/user1.json"), user);
    }

    @Test
    public void testUntyped() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        Map<String, Object> user = mapper.readValue(new File("./src/test/resources/user.json"), Map.class);
        System.out.println(user.get("name"));
        System.out.println(user.get("types").getClass());
        user.put("name", "new name");
        mapper.writeValue(new File("./target/user2.json"), user);
    }

    @Test
    public void write() throws JsonGenerationException, JsonMappingException, IOException {
        Map<String, Object> userData = new HashMap<String, Object>();
        Map<String, String> nameStruct = new HashMap<String, String>();
        nameStruct.put("first", "Joe");
        nameStruct.put("last", "Sixpack");
        userData.put("name", nameStruct);
        userData.put("gender", "MALE");
        userData.put("verified", Boolean.FALSE);
        userData.put("userImage", "Rm9vYmFyIQ==");
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        mapper.writeValue(new File("./target/user3.json"), userData);
    }

    @Test
    public void write2() throws JsonGenerationException, JsonMappingException, IOException {
        Map<String, Object> location = new HashMap<String, Object>();
        location.put("city", "Brussels");
        location.put("country_code", "BE");
        Map<String, Object> userData = new HashMap<String, Object>();
        userData.put("name", "Datalayer");
        userData.put("location", location);
        userData.put("mission", "Convert Data into Action");
        userData.put("url", "http://datalayer.io");
        userData.put("mail", "info@datalayer.io");
        userData.put("twitter", "@datalayerio");
        userData.put("value", "Team over technology");
        userData.put("innovative", Boolean.TRUE);
        userData.put("technologies", Lists.newArrayList("hadoop", "java", "R"));
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        mapper.writeValue(new File("./target/user3.1.json"), userData);
    }

    @Test
    public void testObjectMapper() throws JsonProcessingException, IOException {
        ObjectMapper m = new ObjectMapper();
        m.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        // can either use mapper.readTree(source), or mapper.readValue(source,
        // JsonNode.class);
        JsonNode rootNode = m.readTree(new FileInputStream(new File("./src/test/resources/user.json")));
        // ensure that "last name" isn't "Xmler"; if is, change to "Jsoner"
        JsonNode nameNode = rootNode.path("name");
        String lastName = nameNode.path("last").getTextValue();
        if ("xmler".equalsIgnoreCase(lastName)) {
            ((ObjectNode) nameNode).put("last", "Jsoner");
        }
        // and write it out:
        m.writeValue(new File("user-modified.json"), rootNode);
    }
    
    @Test
    @Ignore
    public void testStreamingRead() throws JsonGenerationException, IOException {
        JsonFactory f = new JsonFactory();
        JsonParser jp = f.createJsonParser(new File("./src/test/resources/user.json"));
        jp.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        User user = new User();
        jp.nextToken(); // will return JsonToken.START_OBJECT (verify?)
        while (jp.nextToken() != JsonToken.END_OBJECT) {
          String fieldname = jp.getCurrentName();
          jp.nextToken(); // move to value, or START_OBJECT/START_ARRAY
          if ("name".equals(fieldname)) { // contains an object
            Name name = new Name();
            while (jp.nextToken() != JsonToken.END_OBJECT) {
              String namefield = jp.getCurrentName();
              jp.nextToken(); // move to value
              if ("first".equals(namefield)) {
                name.setFirst(jp.getText());
              } else if ("last".equals(namefield)) {
                name.setLast(jp.getText());
              } else {
                throw new IllegalStateException("Unrecognized field '"+fieldname+"'!");
              }
            }
            user.setName(name);
          } else if ("gender".equals(fieldname)) {
            user.setGender(User.Gender.valueOf(jp.getText()));
          } else if ("verified".equals(fieldname)) {
            user.setVerified(jp.getCurrentToken() == JsonToken.VALUE_TRUE);
          } else if ("userImage".equals(fieldname)) {
              user.setUserImage(jp.getBinaryValue());
          } else if ("types".equals(fieldname)) {
//              user.setTypes(jp.);
          } else {
            throw new IllegalStateException("Unrecognized field '"+fieldname+"'!");
          }
        }
        jp.close(); // ensure resources get cleaned up timely and properly
    }

    @Test
    public void testStreamingWrite() throws JsonGenerationException, IOException {
        JsonFactory f = new JsonFactory();
        JsonGenerator g = f.createJsonGenerator(new File("./target/user4.json"), JsonEncoding.UTF8);
        g.writeStartObject();
        g.writeObjectFieldStart("name");
        g.writeStringField("first", "Joe");
        g.writeStringField("last", "Sixpack");
        g.writeEndObject(); // for field 'name'
        g.writeStringField("gender", Gender.MALE.name());
        g.writeBooleanField("verified", false);
        g.writeFieldName("userImage"); // no 'writeBinaryField' (yet?)
        byte[] binaryData = new byte[]{};
        g.writeBinary(binaryData);
        g.writeEndObject();
        g.close(); // important: will force flushing of output, close underlying output stream
    }

}
