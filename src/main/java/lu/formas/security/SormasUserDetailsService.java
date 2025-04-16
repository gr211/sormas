package lu.formas.security;

import lombok.val;
import lu.formas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Objects;

public class SormasUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityService securityService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        val authenticatedUser = securityService.getAuthenticatedUser();

        if (Objects.nonNull(authenticatedUser)) {
            return authenticatedUser;
        } else {
            val patient = userRepository.findByEmail(username);

            if (patient.isPresent()) {
                return new User(patient.get().getEmail(), patient.get().getPassword(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
            }
        }

        throw new UsernameNotFoundException("User not found");
    }
}
