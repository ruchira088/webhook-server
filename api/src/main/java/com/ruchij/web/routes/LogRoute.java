package com.ruchij.web.routes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Handler;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import io.javalin.router.JavalinDefaultRoutingApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.prefixPath;

public class LogRoute implements EndpointGroup {
    private static final Logger logger = LoggerFactory.getLogger(LogRoute.class);

    @Override
    public void addEndpoints() {
        JavalinDefaultRoutingApi<?> routingApi = ApiBuilder.staticInstance();
        StringBuilder stringBuilder = new StringBuilder();

        ObjectMapper objectMapper = new ObjectMapper();

        Handler handler = ctx -> {
            stringBuilder.append("\n%s %s\n\n".formatted(ctx.handlerType().name(), ctx.path()));
            for (Map.Entry<String, String> entry : ctx.headerMap().entrySet()) {
                stringBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }

            if (ctx.contentType() != null) {
                stringBuilder.append("\n");

                if (ctx.contentType().contains("application/json")) {
                    JsonNode jsonNode = objectMapper.readValue(ctx.bodyAsBytes(), JsonNode.class);
                    stringBuilder.append(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode));
                } else {
                    stringBuilder.append(ctx.body());
                }
            }

            logger.info(stringBuilder.toString());

            ctx
                    .status(HttpStatus.OK)
                    .json(Map.of());
        };

        for (HandlerType handlerType : HandlerType.values()) {
            if (handlerType.isHttpMethod()) {
                routingApi.addHttpHandler(handlerType, prefixPath("*"), handler);
            }
        }
    }
}
