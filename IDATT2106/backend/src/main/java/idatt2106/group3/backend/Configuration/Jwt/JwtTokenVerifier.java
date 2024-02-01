package idatt2106.group3.backend.Configuration.Jwt;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import idatt2106.group3.backend.Component.JWTHandler;

/**
 * Filter for verifying a token given in request header
 */
public class JwtTokenVerifier extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenVerifier.class);

    /**
     * Verifies the JWT token from request, and returns a 403 if it has expired
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        LOGGER.info("doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) is called");

        // If request tries to create user, it will not try to verify token, but continue the through filters until it gets to registration endpoint
        if((request.getRequestURI().equals("/api/v1/users") && request.getMethod().equals("POST")) || Pattern.matches("/api/v1/websocket/.+", request.getRequestURI())) { 
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getHeader("Authorization");
        JWTHandler.verifyToken(token);
            // Sends the request to the next filter, which will be exception-handler and then the controller
        filterChain.doFilter(request, response);
        
    }
    
}
