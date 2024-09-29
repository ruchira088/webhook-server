package com.ruchij.web.routes;

import com.ruchij.web.responses.ErrorResponse;
import io.javalin.apibuilder.EndpointGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

import static io.javalin.apibuilder.ApiBuilder.get;

public class SearchRoute implements EndpointGroup {
    private static final Logger logger = LoggerFactory.getLogger(SearchRoute.class);

    @Override
    public void addEndpoints() {
        ClassLoader classLoader = getClass().getClassLoader();

        get("/{searchTerm}", context -> {
            String searchTerm = context.pathParam("searchTerm");

            logger.info("Incoming search request for \"%s\"".formatted(searchTerm));

            InputStream inputStream = classLoader.getResourceAsStream("data/" + searchTerm + ".json");
            if (inputStream != null) {
                context
                        .header("Content-Type", "application/json")
                        .result(inputStream.readAllBytes());
            } else {
                context
                        .status(404)
                        .json(new ErrorResponse(new String[]{"Invalid search term provided"}));
            }
        });
    }
}
