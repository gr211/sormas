package lu.formas.services;

import jakarta.persistence.EntityManager;
import lombok.SneakyThrows;
import lombok.val;
import lu.formas.Application;
import lu.formas.repository.PatientRepository;
import lu.formas.repository.VaccineRepository;
import lu.formas.repository.model.Patient;
import lu.formas.repository.model.PatientVaccine;
import lu.formas.repository.model.Vaccine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@Transactional
class NotificationServiceITTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    VaccineRepository vaccineRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    PatientService patientService;

    @Autowired
    NotificationService notificationService;

    @Test
    @SneakyThrows
    public void test_Get_Next_Vaccines() {
        val inFutureYetVaccinatedAlready = new Vaccine();
        inFutureYetVaccinatedAlready.setId(0L);
        inFutureYetVaccinatedAlready.setName("Vaccine");
        inFutureYetVaccinatedAlready.setMaturityMonth(3);
        vaccineRepository.save(inFutureYetVaccinatedAlready);

        val inFuture = new Vaccine();
        inFuture.setId(1L);
        inFuture.setName("Vaccine");
        inFuture.setMaturityMonth(15);
        vaccineRepository.save(inFuture);

        val patient = new Patient();
        patient.setDob(LocalDate.now().minusDays(1).minusMonths(1));
        patient.setPatientVaccines(Collections.singleton(new PatientVaccine() {{
            setVaccine(inFutureYetVaccinatedAlready);
            setPatient(patient);
        }}));

        patientRepository.save(patient);

        entityManager.flush();
        entityManager.clear();

        val nextVaccines = notificationService.nextVaccines(patient);
        assertEquals(Arrays.asList(inFuture), nextVaccines);

        val nextVaccinesByEmail = notificationService.nextVaccines(patient.getEmail());
        assertEquals(Arrays.asList(inFuture), nextVaccinesByEmail);
    }


    @Test
    @SneakyThrows
    public void test_Get_Overdue_Vaccines() {
        val vaccine1 = new Vaccine();
        vaccine1.setId(0L);
        vaccine1.setName("Vaccine");
        vaccine1.setMaturityMonth(3);
        vaccineRepository.save(vaccine1);

        val vaccine2 = new Vaccine();
        vaccine2.setId(1L);
        vaccine2.setName("Vaccine");
        vaccine2.setMaturityMonth(8);
        vaccineRepository.save(vaccine2);

        val patient = new Patient();
        patient.setDob(LocalDate.now().minusDays(1).minusMonths(12));
        patientRepository.save(patient);

        entityManager.flush();
        entityManager.clear();

        val nextVaccines = notificationService.overdueVaccines(patient);
        assertEquals(Arrays.asList(vaccine1, vaccine2), nextVaccines);

        val nextVaccinesByEmail = notificationService.overdueVaccines(patient.getEmail());
        assertEquals(Arrays.asList(vaccine1, vaccine2), nextVaccinesByEmail);
    }
}