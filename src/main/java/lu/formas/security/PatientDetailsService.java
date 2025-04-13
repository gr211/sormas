package lu.formas.security;

import lombok.val;
import lu.formas.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

public class PatientDetailsService implements UserDetailsService {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        val patient = patientRepository.findByEmail(username);

        if (!patient.isPresent()) {
            throw new UsernameNotFoundException("Patient not found");
        }

        return new User(patient.get().getEmail(), patient.get().getPassword(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
