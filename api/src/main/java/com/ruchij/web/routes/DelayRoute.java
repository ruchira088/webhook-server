package com.ruchij.web.routes;

import com.ruchij.web.responses.DelayedResponse;
import io.javalin.apibuilder.EndpointGroup;

import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static io.javalin.apibuilder.ApiBuilder.get;

public class DelayRoute implements EndpointGroup {
    private final ScheduledExecutorService scheduledExecutorService;
    private final Clock clock;

    public DelayRoute(ScheduledExecutorService scheduledExecutorService, Clock clock) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.clock = clock;
    }

    @Override
    public void addEndpoints() {
        get("/{delay}", context -> {
            Long delay = context.pathParamAsClass("delay", Long.class).getOrDefault(1000L);
            Instant start = clock.instant();
            context.future(() -> {
                CompletableFuture<Void> response = new CompletableFuture<>();
                scheduledExecutorService.schedule(() -> {
                    Instant end = clock.instant();
                    long diff = end.toEpochMilli() - start.toEpochMilli();
                    DelayedResponse delayedResponse =
                            new DelayedResponse(delay, TimeUnit.MILLISECONDS, start, end, diff);
                    context.status(200).json(delayedResponse);
                    response.complete(null);
                }, delay, TimeUnit.MILLISECONDS);

                return response;
            });
        });
    }
}
