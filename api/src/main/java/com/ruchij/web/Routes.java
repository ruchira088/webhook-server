package com.ruchij.web;

import com.ruchij.service.health.HealthService;
import com.ruchij.web.routes.DelayRoute;
import com.ruchij.web.routes.LogRoute;
import com.ruchij.web.routes.ServiceRoute;
import io.javalin.apibuilder.EndpointGroup;

import java.time.Clock;
import java.util.concurrent.ScheduledExecutorService;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes implements EndpointGroup {
    private final ServiceRoute serviceRoute;
    private final LogRoute logRoute;
    private final DelayRoute delayRoute;

    public Routes(HealthService healthService, ScheduledExecutorService scheduledExecutorService, Clock clock) {
        this(new ServiceRoute(healthService), new LogRoute(), new DelayRoute(scheduledExecutorService, clock));
    }

    public Routes(ServiceRoute serviceRoute, LogRoute logRoute, DelayRoute delayRoute) {
        this.serviceRoute = serviceRoute;
        this.logRoute = logRoute;
        this.delayRoute = delayRoute;
    }

    @Override
    public void addEndpoints() {
        path("service", serviceRoute);
        path("log", logRoute);
        path("delay", delayRoute);
    }
}
