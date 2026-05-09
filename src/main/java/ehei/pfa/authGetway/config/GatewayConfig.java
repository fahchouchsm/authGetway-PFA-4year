package ehei.pfa.authGetway.config;

import ehei.pfa.authGetway.DTO.Microservice;
import ehei.pfa.authGetway.database.entity.User;
import ehei.pfa.authGetway.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class GatewayConfig {
    private final UserRepository userRepository;

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    public RouterFunction<ServerResponse> routes() {
        return Microservice.getMicroservices().stream()
                .map(this::buildRoute)
                .reduce(RouterFunction::and)
                .orElseThrow(() -> new IllegalStateException("No microservices configured"));
    }

    private RouterFunction<ServerResponse> buildRoute(Microservice ms) {
        return GatewayRouterFunctions.route(ms.getId())
                .route(request -> request.path().startsWith(ms.getPath()), HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri(ms.getIpAddress()))
                .before(this::addAuthHeaders)
                .build();
    }

    private ServerRequest addAuthHeaders(ServerRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String userId = (String) auth.getPrincipal();
            String role = auth.getAuthorities().iterator().next().getAuthority();

            ServerRequest.Builder builder = ServerRequest.from(request)
                    .header("X-User-Id", userId)
                    .header("X-User-Role", role);

            if (request.path().endsWith("/register")) {
                User user = userRepository.findById(userId).orElseThrow();
                builder.header("X-User-Email", user.getEmail())
                        .header("X-User-Name", user.getName())
                        .header("X-User-LastName", user.getLastName());
            }

            return builder.build();
        }
        return request;
    }
}