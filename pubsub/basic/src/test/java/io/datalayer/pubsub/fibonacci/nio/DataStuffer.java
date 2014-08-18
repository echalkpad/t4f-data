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

public class DataStuffer {

  private static byte[] data = new byte[255];
  
  public static void main(String... args) throws IOException {
    
    int port = 9000;
    for (int i = 0; i < data.length; i++) data[i] = (byte) i;
    
    ServerSocket server = new ServerSocket(port);
    while (true) {
      Socket socket = server.accept();
      Thread stuffer = new StuffThread(socket);
      stuffer.start();
    }
    
  }
  
  private static class StuffThread extends Thread {

    private Socket socket;
    
    public StuffThread(Socket socket) {
      this.socket = socket;
    }
    
    public void run() {
      try {
        OutputStream out = new BufferedOutputStream(socket.getOutputStream());
        while (!socket.isClosed()) {
          out.write(data);
        }
      }
      catch (IOException ex) {
        if (!socket.isClosed()) {
          try {
            socket.close();
          } 
          catch (IOException e) {
            // Oh well. We tried
          }
        }
      }
    }
  }
}
