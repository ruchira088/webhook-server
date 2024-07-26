package com.ruchij.web.routes;

import com.ruchij.web.responses.ErrorResponse;
import io.javalin.apibuilder.EndpointGroup;

import java.io.InputStream;

import static io.javalin.apibuilder.ApiBuilder.get;

public class SearchRoute implements EndpointGroup {
    @Override
    public void addEndpoints() {
        ClassLoader classLoader = getClass().getClassLoader();

        get("/{searchTerm}", context -> {
            String searchTerm = context.pathParam("searchTerm");
            InputStream inputStream = classLoader.getResourceAsStream("data/" + searchTerm + ".json");
            if (inputStream != null) {
                context.result(inputStream);
            } else {
                context
                        .status(404)
                        .json(new ErrorResponse(new String[]{"Invalid search term provided"}));
            }
        });
    }
}
