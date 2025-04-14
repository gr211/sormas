package lu.formas.services;

import lombok.val;
import lu.formas.repository.PatientRepository;
import lu.formas.repository.model.Login;
import lu.formas.repository.model.Patient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientService {
    private final PatientRepository repository;
    private final PasswordEncoder passwordEncoder;

    public PatientService(PatientRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(Patient patient) {
        // Encode the password before saving
        val encodedPassword = passwordEncoder.encode(patient.getPassword());
        patient.setPassword(encodedPassword);
        repository.save(patient);
    }

    public Boolean login(Login login) {
        val maybePatient = repository.findByEmail(login.getEmail());

        if (maybePatient.isPresent()) {
            val patient = maybePatient.get();
            return passwordEncoder.matches(login.getPassword(), patient.getPassword());
        }
        return false;
    }

    public boolean exists(Patient patient) {
        return repository.findByEmail(patient.getEmail()).isPresent();
    }

    public Optional<Patient> get(UserDetails userDetails) {
        return repository.findByEmail(userDetails.getUsername());
    }
}
