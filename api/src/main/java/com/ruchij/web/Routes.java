package com.ruchij.web;

import com.ruchij.service.health.HealthService;
import com.ruchij.web.routes.LogRoute;
import com.ruchij.web.routes.ServiceRoute;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes implements EndpointGroup {
    private final ServiceRoute serviceRoute;
    private final LogRoute logRoute;

    public Routes(HealthService healthService) {
        this(new ServiceRoute(healthService), new LogRoute());
    }

    public Routes(ServiceRoute serviceRoute, LogRoute logRoute) {
        this.serviceRoute = serviceRoute;
        this.logRoute = logRoute;
    }

    @Override
    public void addEndpoints() {
        path("service", serviceRoute);
        path("log", logRoute);
    }
}
