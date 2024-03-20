package org.example.client;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.my.proto.NumberGenerationServiceOuterClass.NumbersSequenceResponse;

public class ClientStreamObserver implements StreamObserver<NumbersSequenceResponse> {

    private final static Logger log = LoggerFactory.getLogger(NumbersClient.class);
    private long lastValue = 0;

    @Override
    public void onNext(NumbersSequenceResponse numbersSequenceResponse) {
        log.info("new value: {}", numbersSequenceResponse.getCurrentValue());
        setLastValue(numbersSequenceResponse.getCurrentValue());
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("error!");
    }

    @Override
    public void onCompleted() {
        log.info("request completed");
    }

    private synchronized void setLastValue(int currentValue) {
        this.lastValue = currentValue;
    }

    public synchronized long getLastValueAndReset() {
        long lastValueResponse = this.lastValue;
        this.lastValue = 0;
        return lastValueResponse;
    }

}
