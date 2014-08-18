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
package io.datalayer.pubsub.fibonacci.nio;
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class NewDataStuffer {

  private static byte[] data = new byte[255];
  
  public static void main(String... args) throws IOException {

    for (int i = 0; i < data.length; i++) data[i] = (byte) i;
    
    ServerSocketChannel server = ServerSocketChannel.open();
    server.configureBlocking(false);
    
    server.socket().bind(new InetSocketAddress(9000));
    Selector selector = Selector.open();
    server.register(selector, SelectionKey.OP_ACCEPT);
      
    while (true) {
      selector.select();
      Set readyKeys = selector.selectedKeys();
      Iterator iterator = readyKeys.iterator();
      while (iterator.hasNext()) {
        SelectionKey key = (SelectionKey) iterator.next();
        iterator.remove();
        try {
          if (key.isAcceptable()) {
            SocketChannel client = server.accept();
            System.out.println("Accepted connection from " + client);
            client.configureBlocking(false);
            ByteBuffer source = ByteBuffer.wrap(data);
            SelectionKey key2 = client.register(selector, SelectionKey.OP_WRITE);
            key2.attach(source);
          }
          else if (key.isWritable()) {
            SocketChannel client = (SocketChannel) key.channel();
            ByteBuffer output = (ByteBuffer) key.attachment();
            if (!output.hasRemaining()) {
              output.rewind();
            }
            client.write(output);
          }
        }
        catch (IOException ex) {
          key.cancel();
          try {
            key.channel().close();
          }
          catch (IOException cex) {}
        } 
      }
    }
  }
}
