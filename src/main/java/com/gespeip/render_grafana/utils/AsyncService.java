package com.gespeip.render_grafana.utils;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class AsyncService {

    @Async
    public CompletableFuture<String> getWaitingPage() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            System.out.println("wait");
        }
        return CompletableFuture.completedFuture("waiting page");
    }
    @Async
    public CompletableFuture<String> getResultPage() throws InterruptedException
    {
        Thread.sleep(5000);
        System.out.println("result");
        return CompletableFuture.completedFuture("result page");
    }
}
