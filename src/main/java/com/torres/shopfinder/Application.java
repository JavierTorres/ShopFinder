package com.torres.shopfinder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
public class Application extends AsyncConfigurerSupport {

    @Value("${executor.core.pool.size}")
    private int executorPoolSize;

    @Value("${executor.max.pool.size}")
    private int executorMaxPoolSize;

    @Value("${executor.queue.capacity}")
    private int executorQueueCapacity;

    @Value("${executor.thread.name.prefix}")
    private String executorThreadNamePrefix;


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(executorPoolSize);
        executor.setMaxPoolSize(executorMaxPoolSize);
        executor.setQueueCapacity(executorQueueCapacity);
        executor.setThreadNamePrefix(executorThreadNamePrefix);
        executor.initialize();
        return executor;
    }
}
