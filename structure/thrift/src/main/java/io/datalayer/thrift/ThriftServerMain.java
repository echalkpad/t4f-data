package io.datalayer.thrift;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

import tutorial.Calculator;

public class ThriftServerMain {

    public static void main(String[] args) throws TTransportException {

        CalculatorHandler handler = new CalculatorHandler();
        Calculator.Processor processor = new Calculator.Processor(handler);
        TServerTransport serverTransport = new TServerSocket(9090);
        TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

        System.out.println("Starting the simple server...");
        server.serve();
        
    }

}
