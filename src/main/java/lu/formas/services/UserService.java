package lu.formas.services;

import lombok.val;
import lu.formas.repository.PatientRepository;
import lu.formas.repository.UserRepository;
import lu.formas.repository.model.Login;
import lu.formas.repository.model.Patient;
import lu.formas.repository.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PatientRepository patientRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = repository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(User user) {
        // Encode the password before saving
        val encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        val patient = new Patient();
        patient.setEmail(user.getEmail());
        patientRepository.save(patient);
    }

    public Boolean login(Login login) {
        val maybeUser = userRepository.findByEmail(login.getEmail());

        if (maybeUser.isPresent()) {
            val User = maybeUser.get();
            return passwordEncoder.matches(login.getPassword(), User.getPassword());
        }
        return false;
    }

    public void delete(User User) {
        userRepository.deleteUserByEmail(User.getEmail());
        patientRepository.deletePatientByEmail(User.getEmail());
    }

    public boolean exists(User User) {
        return userRepository.findByEmail(User.getEmail()).isPresent();
    }

    public Optional<User> get(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername());
    }

    public Optional<User> byEMail(String email) {
        return userRepository.findByEmail(email);
    }
}
