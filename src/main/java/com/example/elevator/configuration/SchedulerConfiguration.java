package com.example.elevator.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class SchedulerConfiguration {

    @Value("${elevators.threadPool.size}")
    private int threadPoolSize;

    @Bean
    public ScheduledExecutorService elevatorScheduledExecutorService() {
        return Executors.newScheduledThreadPool(threadPoolSize);
    }
}
