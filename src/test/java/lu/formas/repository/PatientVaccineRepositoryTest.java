package lu.formas.repository;

import jakarta.persistence.EntityManager;
import lombok.val;
import lu.formas.repository.model.Patient;
import lu.formas.repository.model.PatientVaccine;
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
    void creating_abc() {
        val patient = new Patient();
        patient.setEmail("abc@gmail.com");
        patient.setPassword("123456");
        patient.setFirstName("abc");
        patient.setLastName("def");

        val vaccine = vaccineRepository.findAll().get(0);

        val patientVaccine = new PatientVaccine();
        patientVaccine.setVaccine(vaccine);
        patientVaccine.setPatient(patient);

        patient.setPatientVaccines(new HashSet<>(Arrays.asList(patientVaccine)));

        patientRepository.save(patient);

        entityManager.flush();
        entityManager.clear();

        val patient2 = patientRepository.findByEmail("abc@gmail.com").get();
        assert patient2.getPatientVaccines().stream().findFirst().get().getVaccine().getName().equals("RSV");

        val vaccine2 = vaccineRepository.findAll().get(0); // RSV
        assert vaccine2.getPatientVaccines().stream().findFirst().get().getPatient().getFirstName().equals("abc");
    }

}
