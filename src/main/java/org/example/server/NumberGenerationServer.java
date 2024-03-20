package org.example.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import org.example.server.service.NumberGenerationServiceImpl;

/**
 * Hello world!
 */
public class NumberGenerationServer {

    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException, InterruptedException {

        Server server = ServerBuilder.forPort(PORT)
                .addService(new NumberGenerationServiceImpl())
                .build();

        server.start();

        server.awaitTermination();
    }

}
