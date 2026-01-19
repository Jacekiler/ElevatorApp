package com.example.elevator.scheduler;

import com.example.elevator.service.ElevatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ElevatorEngineScheduler {

    private final ScheduledExecutorService executorService;
    private final ElevatorService elevatorService;

    @EventListener(ApplicationReadyEvent.class)
    public void run() {
        elevatorService.getEngines()
                .forEach(engine -> executorService.scheduleAtFixedRate(engine::runCycle,0,500 , TimeUnit.MILLISECONDS));
    }
}
