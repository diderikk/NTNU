package idatt2106.group3.backend.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import idatt2106.group3.backend.Model.User;
import idatt2106.group3.backend.Model.UserSecurityDetails;
import idatt2106.group3.backend.Repository.UserRepository;

/**
 * Service class needed for getting User from database and creating UserDetails object needed for Security Principal
 */
@Service
public class UserSecurityDetailsService implements UserDetailsService{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSecurityDetailsService.class);

    @Autowired
    UserRepository userRepository;

    /**
     * Overrides the method from UserDetailsService interface. We use email as user's username in our case.
     * Gets user with email from our MySQL database and creates an UserSecurityDetails object and returns its
     * @param email
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.info("loadUserByEmail(String email) was called with email: {}", email);

        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            List<GrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority(user.getRole()));
            return new UserSecurityDetails(user.getHash(), user.getEmail(), user.getUserId(), grantedAuthorities);
        }
        else{
            LOGGER.warn("Could not find user with email: {}. Throwing exception", email);
            throw new UsernameNotFoundException("Email: " + email + "was not found");
        }
    }

}