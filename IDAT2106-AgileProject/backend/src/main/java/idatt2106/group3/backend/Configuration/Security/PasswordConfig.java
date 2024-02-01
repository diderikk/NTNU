package idatt2106.group3.backend.Configuration.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

/**
 * Configures PasswordEncoder for hashing and salting password for users
 * Also checks if an inputted password matches a hashed password
 */
@Configuration
public class PasswordConfig
{
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom());
    }
}

