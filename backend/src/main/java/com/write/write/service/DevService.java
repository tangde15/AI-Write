package com.write.write.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class DevService {
    private static final Logger log = LoggerFactory.getLogger(DevService.class);

    @Async("taskExecutor")
    public void asyncWork(long millis, String note) {
        long start = System.currentTimeMillis();
        log.info("[DEV-ASYNC] start note={} millis={}ms", note, millis);
        try {
            Thread.sleep(Math.max(0, millis));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        long cost = System.currentTimeMillis() - start;
        log.info("[DEV-ASYNC] done note={} cost={}ms", note, cost);
    }
}
