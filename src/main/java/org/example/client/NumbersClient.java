package org.example.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.my.proto.NumberGenerationServiceGrpc;
import ru.my.proto.NumberGenerationServiceGrpc.NumberGenerationServiceStub;
import ru.my.proto.NumberGenerationServiceOuterClass.NumbersSequenceRequest;

public class NumbersClient {

    private final static Logger log = LoggerFactory.getLogger(NumbersClient.class);
    private final static String url = "localhost:8080";
    private static final int LOOP_COUNT = 50;

    private long currentValue;

    public static void main(String[] args) throws InterruptedException {
        log.info("numbers Client is starting...");

        ManagedChannel channel = ManagedChannelBuilder.forTarget(url)
                .usePlaintext()
                .build();

        NumberGenerationServiceGrpc.NumberGenerationServiceStub stub =
                NumberGenerationServiceGrpc.newStub(channel);

        new NumbersClient().processClient(stub);
    }

    private void processClient(NumberGenerationServiceStub stub) throws InterruptedException {
        NumbersSequenceRequest sequenceRequest = getNumbersSequenceRequest();
        ClientStreamObserver streamObserver = new ClientStreamObserver();
        stub.generate(sequenceRequest, streamObserver);

        for (int i = 0; i < LOOP_COUNT; i++) {
            long resultValue = getNextValue(streamObserver);
            log.info("currentValue: {}", resultValue);
            Thread.sleep(1000);
        }
    }

    private long getNextValue(ClientStreamObserver streamObserver) {
        currentValue = currentValue + streamObserver.getLastValueAndReset() + 1;
        return currentValue;
    }

    private NumbersSequenceRequest getNumbersSequenceRequest() {
        return NumbersSequenceRequest.newBuilder()
                .setFirstValue(0)
                .setLastValue(30)
                .build();
    }

}
