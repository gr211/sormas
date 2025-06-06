package lu.sormas.services;

import lombok.val;
import lu.sormas.repository.PatientRepository;
import lu.sormas.repository.UserRepository;
import lu.sormas.repository.model.Login;
import lu.sormas.repository.model.Patient;
import lu.sormas.repository.model.User;
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

    /**
     * At DB level, User and Patient are independent entities, by design. The idea behind that reason is that both entities could have different life-cycles (e.g. a user could be deleted, but the patient should remain in the system for audit purposes).
     * <p>
     * At application level however, we want to keep both entities in sync. So when a user is created, we create a patient as well, within the same transaction boundaries to ensure consistency.
     */
    public void save(User user) {
        // Encode the password before saving
        val encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        val patient = new Patient();
        patient.setEmail(user.getEmail());
        patient.setDob(user.getDob());
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

    /**
     * At DB level, User and Patient are independent entities, by design.
     * <p>
     * For now however, such a requirement is not asked for, so we can delete both entities at once, within a transactional boundaries to avoid any inconsistencies.
     */
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
