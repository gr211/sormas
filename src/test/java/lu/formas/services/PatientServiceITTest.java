package lu.formas.services;

import jakarta.persistence.EntityManager;
import lombok.SneakyThrows;
import lombok.val;
import lu.formas.repository.PatientRepository;
import lu.formas.repository.VaccineRepository;
import lu.formas.repository.model.Patient;
import lu.formas.repository.model.PatientVaccine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
class PatientServiceITTest {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    private VaccineRepository vaccineRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private EntityManager entityManager;

    @Test
    @SneakyThrows
    public void test_Save_With_Vaccines() {
        val service = new PatientService(patientRepository, passwordEncoder);

        val patient = new Patient();
        patient.setEmail("abc@gmail.com");
        patient.setPassword("123456");
        patient.setFirstName("abc");
        patient.setLastName("def");

        val vaccine = vaccineRepository.findAll().get(0); // RSV

        val patientVaccine = new PatientVaccine();
        patientVaccine.setVaccine(vaccine);
        patientVaccine.setPatient(patient);

        patient.setPatientVaccines(new HashSet<>(Arrays.asList(patientVaccine)));

        service.save(patient);

        entityManager.flush();
        entityManager.clear();

        val patient2 = service.byEMail("abc@gmail.com").get();
        assert patient2.getPatientVaccines().stream().findFirst().get().getVaccine().getName().equals("RSV");
    }
}