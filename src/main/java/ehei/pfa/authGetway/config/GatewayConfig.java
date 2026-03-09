package ehei.pfa.authGetway.config;

import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class GatewayConfig {

    @Bean
    public RouterFunction<ServerResponse> routes() {
        return GatewayRouterFunctions.route("dotnet-service")
                .route(request -> request.path().startsWith("/api/"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri("http://localhost:8085"))
                .build();
    }
}