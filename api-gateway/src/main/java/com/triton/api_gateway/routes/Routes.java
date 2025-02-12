package com.triton.api_gateway.routes;

import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;

@Configuration
public class Routes {

    @Bean
    public RouterFunction<ServerResponse> productRouteMatcher() {
        return GatewayRouterFunctions.route("product_service")
                .route(RequestPredicates.path("api/product/**"), HandlerFunctions.http("http://localhost:8081"))
                .filter(
                        CircuitBreakerFilterFunctions.circuitBreaker(
                                "productServiceCircuitBreaker",
                                URI.create("forward:/fallbackRoute")
                        )
                )
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderRouteMatcher() {
        return GatewayRouterFunctions.route("order_service")
                .route(RequestPredicates.path("api/order"), HandlerFunctions.http("http://localhost:8082"))
                .filter(
                        CircuitBreakerFilterFunctions.circuitBreaker(
                                "orderServiceCircuitBreaker",
                                URI.create("forward:/fallbackRoute")
                        )
                )
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryRouteMatcher() {
        return GatewayRouterFunctions.route("inventory_service")
                .route(RequestPredicates.path("api/inventory"), HandlerFunctions.http("http://localhost:8083"))
                .filter(
                        CircuitBreakerFilterFunctions.circuitBreaker(
                                "inventoryServiceCircuitBreaker",
                                URI.create("forward:/fallbackRoute")
                        )
                )
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> productSwaggerRouteMatcher() {
        return GatewayRouterFunctions.route("product_service_swagger")
                .route(RequestPredicates.path("/aggregate/product-service/api-docs"), HandlerFunctions.http("http://localhost:8081"))
                .filter(
                        CircuitBreakerFilterFunctions.circuitBreaker(
                                "productSwaggerServiceCircuitBreaker",
                                URI.create("forward:/fallbackRoute")
                        )
                )
                .filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderSwaggerRouteMatcher() {
        return GatewayRouterFunctions.route("order_service_swagger")
                .route(RequestPredicates.path("/aggregate/order-service/api-docs"), HandlerFunctions.http("http://localhost:8082"))
                .filter(
                        CircuitBreakerFilterFunctions.circuitBreaker(
                                "orderSwaggerServiceCircuitBreaker",
                                URI.create("forward:/fallbackRoute")
                        )
                )
                .filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventorySwaggerRouteMatcher() {
        return GatewayRouterFunctions.route("inventory_service_swagger")
                .route(RequestPredicates.path("/aggregate/inventory-service/api-docs"), HandlerFunctions.http("http://localhost:8083"))
                .filter(
                        CircuitBreakerFilterFunctions.circuitBreaker(
                                "inventorySwaggerServiceCircuitBreaker",
                                URI.create("forward:/fallbackRoute")
                        )
                )
                .filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> fallbackRouteMatcher() {
        return GatewayRouterFunctions.route("fallbackRoute")
                .GET(
                        "/fallbackRoute",
                        request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                            .body("Service is unavailable, please try again later")
                ).build();
    }

}
