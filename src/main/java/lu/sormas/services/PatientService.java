package lu.sormas.services;

import lombok.val;
import lombok.var;
import lu.sormas.repository.PatientRepository;
import lu.sormas.repository.model.Patient;
import lu.sormas.repository.model.PatientVaccine;
import lu.sormas.repository.model.Vaccine;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class PatientService {
    private final PatientRepository repository;

    public PatientService(PatientRepository repository) {
        this.repository = repository;
    }

    public void save(Patient patient) {
        repository.save(patient);
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

    public Optional<Patient> byEmail(String email) {
        return repository.findByEmail(email);
    }

    public void addToVaccines(String email, Vaccine vaccine, LocalDate date, String comments) {
        val patient = byEmail(email).orElseThrow(() -> new RuntimeException("Patient with email " + email + " does not exist"));

        val patientVaccine = new PatientVaccine();
        patientVaccine.setPatient(patient);
        patientVaccine.setVaccine(vaccine);
        patientVaccine.setVaccineDate(date);
        patientVaccine.setComments(comments);

        var patientVaccines = patient.getPatientVaccines();

        if (patientVaccines == null) {
            patientVaccines = new HashSet<>();
        }

        patientVaccines.remove(patientVaccine); // only 1 vaccine of a kind per patient. Remove any previously set
        patientVaccines.add(patientVaccine);

        patient.setPatientVaccines(patientVaccines);
        save(patient);
    }

    public List<PatientVaccine> getVaccinesEntries(String email) {
        val patient = byEmail(email).orElseThrow(() -> new RuntimeException("Patient with email " + email + " does not exist"));
        var patientVaccines = patient.getPatientVaccines();

        if (patientVaccines == null) {
            patientVaccines = new HashSet<>();
        }

        ArrayList<PatientVaccine> patientVaccines1 = new ArrayList<>(patientVaccines);
        patientVaccines1.sort(Comparator.comparing(PatientVaccine::getVaccineDate));
        return patientVaccines1;
    }

    public void removeVaccination(String email, PatientVaccine patientVaccine) {
        val patient = byEmail(email).orElseThrow(() -> new RuntimeException("Patient with email " + email + " does not exist"));

        var patientVaccines = patient.getPatientVaccines();

        if (Objects.isNull(patientVaccines)) {
            return;
        }

        patientVaccines.remove(patientVaccine);
        patient.setPatientVaccines(patientVaccines);
        save(patient);
    }
}
