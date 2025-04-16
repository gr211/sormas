package lu.formas.services;

import jakarta.persistence.EntityManager;
import lombok.SneakyThrows;
import lombok.val;
import lu.formas.repository.PatientRepository;
import lu.formas.repository.VaccineRepository;
import lu.formas.repository.model.Patient;
import lu.formas.repository.model.PatientVaccine;
import lu.formas.repository.model.Vaccine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
        val service = new PatientService(patientRepository);

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

        patient.setPatientVaccines(new HashSet<>(Collections.singletonList(patientVaccine)));

        service.save(patient);

        entityManager.flush();
        entityManager.clear();

        val patient2 = service.byEMail("abc@gmail.com").get();
        assert patient2.getPatientVaccines().stream().findFirst().get().getVaccine().getName().equals("Vaccine");
    }

    @Test
    @SneakyThrows
    public void test_AddVaccines_To_Patient() {
        val service = new PatientService(patientRepository);

        val patient = new Patient();
        patient.setEmail("abc@gmail.com");

        service.save(patient);

        val vaccine1 = new Vaccine();
        vaccine1.setId(1L);
        vaccine1.setName("Vaccine");
        vaccine1.setGoals("Goals");
        vaccine1.setDescription("Description");

        val vaccine2 = new Vaccine();
        vaccine2.setId(2L);
        vaccine2.setName("Vaccine2");
        vaccine2.setGoals("Goals");
        vaccine2.setDescription("Description");

        vaccineRepository.save(vaccine1);
        vaccineRepository.save(vaccine2);

        {
            service.addToVaccines(patient.getEmail(), vaccine1, LocalDate.now(), "comments");

            entityManager.flush();
            entityManager.clear();

            val patient2 = service.byEMail("abc@gmail.com").get();
            Assertions.assertEquals("Vaccine", patient2.getPatientVaccines().stream().findFirst().get().getVaccine().getName());
        }

        {
            service.addToVaccines(patient.getEmail(), vaccine2, LocalDate.now().minusYears(1), "comments");

            entityManager.flush();
            entityManager.clear();

            val patient2 = service.byEMail("abc@gmail.com").get();

            val vaccines = new ArrayList<>(patient2.getPatientVaccines());
            Assertions.assertEquals(2, vaccines.size());
        }
    }

    @Test
    @SneakyThrows
    public void test_Add_SameVaccine_Twice_Last_One_Wins() {
        val service = new PatientService(patientRepository);

        val patient = new Patient();
        patient.setEmail("abc@gmail.com");

        service.save(patient);

        val vaccine1 = new Vaccine();
        vaccine1.setId(1L);
        vaccine1.setName("Vaccine");
        vaccine1.setGoals("Goals");
        vaccine1.setDescription("Description");

        val vaccine2 = new Vaccine();
        vaccine2.setId(2L);
        vaccine2.setName("Vaccine2");
        vaccine2.setGoals("Goals");
        vaccine2.setDescription("Description");

        vaccineRepository.save(vaccine1);
        vaccineRepository.save(vaccine2);

        service.addToVaccines(patient.getEmail(), vaccine1, LocalDate.now(), "comments");
        service.addToVaccines(patient.getEmail(), vaccine2, LocalDate.now(), "comments");
        service.addToVaccines(patient.getEmail(), vaccine1, LocalDate.now(), "comments");

        entityManager.flush();
        entityManager.clear();

        val patient2 = service.byEMail("abc@gmail.com").get();
        val vaccines = new ArrayList<>(patient2.getPatientVaccines());
        Assertions.assertEquals(2, vaccines.size());
    }
}