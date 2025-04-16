package lu.formas.repository;

import jakarta.persistence.EntityManager;
import lombok.val;
import lu.formas.repository.model.Patient;
import lu.formas.repository.model.PatientVaccine;
import lu.formas.repository.model.Vaccine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
public class PatientVaccineRepositoryTest {

    @Autowired
    private VaccineRepository vaccineRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private EntityManager entityManager;


    @Test
    void associating_vaccines_to_patients() {
        val patient = new Patient();
        patient.setEmail("abc@gmail.com");

        val vaccine = new Vaccine();
        vaccine.setId(1L);
        vaccine.setName("Vaccine");
        vaccine.setGoals("Goals");
        vaccine.setDescription("Description");

        vaccineRepository.save(vaccine);

        val patientVaccine = new PatientVaccine();
        patientVaccine.setVaccine(vaccine);
        patientVaccine.setPatient(patient);

        patient.setPatientVaccines(new HashSet<>(Arrays.asList(patientVaccine)));

        patientRepository.save(patient);

        entityManager.flush();
        entityManager.clear();

        val patient2 = patientRepository.findByEmail("abc@gmail.com").get();
        Assertions.assertEquals("Vaccine", patient2.getPatientVaccines().stream().findFirst().get().getVaccine().getName());

        val vaccine2 = vaccineRepository.findAll().get(0); // RSV
        Assertions.assertEquals("abc@gmail.com", vaccine2.getPatientVaccines().stream().findFirst().get().getPatient().getEmail());
    }

}
