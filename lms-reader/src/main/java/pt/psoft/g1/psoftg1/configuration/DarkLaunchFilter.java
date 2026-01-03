package pt.psoft.g1.psoftg1.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class DarkLaunchFilter extends OncePerRequestFilter {
    @Value("${darklaunch.secret.header}")
    private String betaHeader;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String headerValue = request.getHeader(betaHeader);
        if ("true".equals(headerValue)) {
            logger.info("Dark Launch access triggered!");
        }
        filterChain.doFilter(request, response);
    }
}