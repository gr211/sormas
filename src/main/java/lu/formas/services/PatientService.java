package lu.formas.services;

import lombok.val;
import lombok.var;
import lu.formas.repository.PatientRepository;
import lu.formas.repository.model.Login;
import lu.formas.repository.model.Patient;
import lu.formas.repository.model.PatientVaccine;
import lu.formas.repository.model.Vaccine;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

@Service
@Transactional
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

    public void delete(Patient patient) {
        repository.deletePatientByEmail(patient.getEmail());
    }

    public boolean exists(Patient patient) {
        return repository.findByEmail(patient.getEmail()).isPresent();
    }

    public Optional<Patient> get(UserDetails userDetails) {
        return repository.findByEmail(userDetails.getUsername());
    }

    public Optional<Patient> byEMail(String email) {
        return repository.findByEmail(email);
    }

    public void addToVaccines(String email, Vaccine vaccine, LocalDate date) {
        val patient = byEMail(email).orElseThrow(() -> new RuntimeException("Patient with email " + email + " does not exist"));
        ;

        val patientVaccine = new PatientVaccine();
        patientVaccine.setPatient(patient);
        patientVaccine.setVaccine(vaccine);
        patientVaccine.setVaccineDate(date);

        var patientVaccines = patient.getPatientVaccines();

        if (patientVaccines == null) {
            patientVaccines = new HashSet<>();
        }

        patientVaccines.remove(patientVaccine); // only 1 vaccine of a kind per patient. Remove any previously set
        patientVaccines.add(patientVaccine);

        patient.setPatientVaccines(patientVaccines);
        save(patient);
    }
}
