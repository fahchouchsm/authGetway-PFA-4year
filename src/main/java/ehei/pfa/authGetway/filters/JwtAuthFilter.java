package ehei.pfa.authGetway.filters;

import ehei.pfa.authGetway.security.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements Filter {

    private final JwtUtil jwtUtil;
    private final StringRedisTemplate redis;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                if (Boolean.TRUE.equals(redis.hasKey("blacklist:" + token))) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Token has been revoked");
                    return;
                }

                var claims = jwtUtil.parseClaims(token);
                String userId = claims.getSubject();
                String role = claims.get("role", String.class);

                HttpServletRequestWrapper mutated = new HttpServletRequestWrapper(request) {
                    @Override
                    public String getHeader(String name) {
                        if ("X-User-Id".equals(name)) return userId;
                        if ("X-User-Role".equals(name)) return role;
                        return super.getHeader(name);
                    }
                };

                var auth = new UsernamePasswordAuthenticationToken(
                        userId, null, List.of(() -> "ROLE_" + role)
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
                chain.doFilter(mutated, res);
                return;

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired token");
                return;
            }
        }

        chain.doFilter(req, res);
    }
}