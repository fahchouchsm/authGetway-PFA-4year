package ehei.pfa.authGetway.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ReqLogFilter extends OncePerRequestFilter {

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        long start = System.currentTimeMillis();
        filterChain.doFilter(request, response);
        long time = System.currentTimeMillis() - start;

        int status = response.getStatus();
        String color;

        if (status >= 200 && status < 300) {
            color = GREEN;
        } else if (status >= 300 && status < 400) {
            color = YELLOW;
        } else {
            color = RED;
        }

        System.out.println(request.getMethod() + " " +
                request.getRequestURI() + " -> " +
                color + status + RESET + " (" + time + "ms)");
    }
}
