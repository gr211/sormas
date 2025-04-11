package lu.formas.services;

import lu.formas.repository.PatientRepository;
import lu.formas.repository.model.Patient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        String encodedPassword = passwordEncoder.encode(patient.getPassword());
        patient.setPassword(encodedPassword);
        repository.save(patient);
    }

    public boolean exists(Patient patient) {
        return repository.findByEmail(patient.getEmail()).isPresent();
    }
}
