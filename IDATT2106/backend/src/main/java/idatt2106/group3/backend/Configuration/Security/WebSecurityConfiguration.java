package idatt2106.group3.backend.Configuration.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import idatt2106.group3.backend.Configuration.Jwt.JwtTokenVerifier;
import idatt2106.group3.backend.Configuration.Jwt.JwtUsernameAndPasswordAuthenticationFilter;
import idatt2106.group3.backend.Service.UserSecurityDetailsService;

/**
 * Configures WebSecurity for endpoints
 * Not used for tests
 */
@Profile("!test")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserSecurityDetailsService userSecurityDetailsService;

    /**
     * Method for configuration of http.
     * CSRF is currently countered by CORS, and this app is only used on localhost currently
     * Authentication by username and password, and JWT.
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .addFilter(getJWTAuthenticationFilter(authenticationManager()))
        .addFilterAfter(new JwtTokenVerifier(), JwtUsernameAndPasswordAuthenticationFilter.class)
        .addFilterAfter(new ExceptionHandlerFilter(), JwtTokenVerifier.class)
        .authorizeRequests()
        .antMatchers("/error").permitAll()
        .antMatchers("/api/v1/activities/**").hasAnyRole("USER", "ADMIN")
        .antMatchers(HttpMethod.POST,"/api/v1/users").permitAll()
        .antMatchers("/api/v1/websocket/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .cors();
    }

    /**
     * Sets authentication provider defined in method under
     * @param auth
     * @throws Exception
     */ 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    /**
     * Sets passwordEncoder and userDetailsService from service folder
     * Needed for getting users from MySQL Database and not In-Memory database
     * Allows authentication from MySQL Database
     * @return DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userSecurityDetailsService);
        return provider;
    }
    /**
     * Changes login url
     * @param authenticationManager
     * @return Username and password filter for login
     */
     public JwtUsernameAndPasswordAuthenticationFilter getJWTAuthenticationFilter(AuthenticationManager authenticationManager){
        final JwtUsernameAndPasswordAuthenticationFilter filter = new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager);
        filter.setFilterProcessesUrl("/api/v1/login");
        return filter;
    }
}
