package idatt2106.group3.backend.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import idatt2106.group3.backend.Configuration.Jwt.JwtSigningKey;
import idatt2106.group3.backend.Model.UserSecurityDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

/**
 * Handler class for JWT (JSON Web Tokens)
 */
public class JWTHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(JWTHandler.class);

    private JWTHandler(){}

    /**
     * Method for verifying JWT. This is important to
     * make sure that a user has the rights to excecute 
     * authorised tasks.
     * @param token
     * @return Authentication created from the JWT
     */
    public static Authentication verifyToken(String token){
        try{
            //Parses and verifies token
            Jws<Claims> claimsJWs = Jwts.parserBuilder()
                .setSigningKey(JwtSigningKey.getInstance())
                .build().parseClaimsJws(token);
            Claims body = claimsJWs.getBody();

            String username = body.getSubject();

            int userId = (Integer)body.get("userId");
            
            List<Map<String, String>> authoritiesMapping = (List<Map<String, String>>) body.get("authorities");

            List<GrantedAuthority> grantedAuthorities = authoritiesMapping.stream()
            .map(auth -> new SimpleGrantedAuthority(auth.get("authority"))).collect(Collectors.toList());

            UserSecurityDetails userSecurityDetails = new UserSecurityDetails("", username, userId, grantedAuthorities);

            // Creates an authentication from the JWT token
            Authentication authentication = new UsernamePasswordAuthenticationToken(userSecurityDetails, token, grantedAuthorities);

            // Sets authentication
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return authentication;

        }catch(JwtException e){
            LOGGER.warn("Something went wrong trying to verify JWT token: {}\nException message: {}", token, e.getMessage());
            return null;
        }
    }
}
