package ehei.pfa.authGetway.config;

import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class GatewayConfig {

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    public RouterFunction<ServerResponse> routes() {
        return GatewayRouterFunctions.route("dotnet-service")
                .route(request -> request.path().startsWith("/api/city/"),
                        HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri("http://localhost:5181"))
                .before(request -> {
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    if (auth != null && auth.isAuthenticated()) {
                        String userId = (String) auth.getPrincipal();
                        String role = auth.getAuthorities().iterator().next().getAuthority();
                        return ServerRequest.from(request)
                                .header("X-User-Id", userId)
                                .header("X-User-Role", role)
                                .build();
                    }
                    return request;
                })
                .build();
    }
}