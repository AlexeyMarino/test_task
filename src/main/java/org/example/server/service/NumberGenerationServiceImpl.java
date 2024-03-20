package org.example.server.service;

import io.grpc.stub.StreamObserver;
import ru.my.proto.NumberGenerationServiceGrpc;
import ru.my.proto.NumberGenerationServiceOuterClass;

public class NumberGenerationServiceImpl extends NumberGenerationServiceGrpc.NumberGenerationServiceImplBase {

    private static final int TIMEOUT = 2000;

    @Override
    public void generate(NumberGenerationServiceOuterClass.NumbersSequenceRequest request,
                         StreamObserver<NumberGenerationServiceOuterClass.NumbersSequenceResponse> responseObserver) {
        int firstValue = request.getFirstValue();
        int lastValue = request.getLastValue();

        for (int i = firstValue; i <= lastValue; i++) {
            sendValue(i, responseObserver);
            try {
                Thread.sleep(TIMEOUT);
            } catch (InterruptedException e) {
                responseObserver.onError(e);
                throw new RuntimeException(e);
            }
        }

        responseObserver.onCompleted();

    }

    private void sendValue(int i,
                           StreamObserver<NumberGenerationServiceOuterClass.NumbersSequenceResponse> responseObserver) {
        NumberGenerationServiceOuterClass.NumbersSequenceResponse response =
                NumberGenerationServiceOuterClass.NumbersSequenceResponse.newBuilder()
                        .setCurrentValue(i)
                        .build();
        responseObserver.onNext(response);
    }

}
