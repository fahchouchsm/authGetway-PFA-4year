package ehei.pfa.authGetway.filters;

import ehei.pfa.authGetway.security.JwtUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

public class JwtAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                var claims = JwtUtil.parseClaims(token);
                String userId = claims.getSubject();
                String role = claims.get("role", String.class);

                var auth = new UsernamePasswordAuthenticationToken(
                        userId, null, List.of(() -> "ROLE_" + role)
                );
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                HttpServletResponse httpRes = (HttpServletResponse) res;
                httpRes.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpRes.getWriter().write("Invalid or expired token");
                return;
            }
        }

        chain.doFilter(req, res);
    }
}