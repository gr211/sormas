package lu.formas.services;

import lombok.SneakyThrows;
import lombok.val;
import lu.formas.Application;
import lu.formas.repository.model.Patient;
import lu.formas.repository.model.PatientVaccine;
import lu.formas.repository.model.Vaccine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@AutoConfigureMockMvc
class NotificationServiceTest {
    @MockitoBean
    PatientService patientService;

    @MockitoBean
    VaccineService vaccineService;

    @Test
    @SneakyThrows
    public void test_Calculate_Age_In_Months() {
        val service = new NotificationService(patientService, vaccineService);

        val patient = new Patient();

        patient.setDob(LocalDate.now().minusMonths(1));
        assertEquals(0, service.ageInMonths(patient)); //not quite just a month old yet

        patient.setDob(LocalDate.now().minusDays(1).minusMonths(1));
        assertEquals(1, service.ageInMonths(patient));

        patient.setDob(LocalDate.now().minusDays(1).minusMonths(12));
        assertEquals(12, service.ageInMonths(patient));
    }

    @Test
    @SneakyThrows
    public void test_Get_Next_Vaccine() {
        val service = new NotificationService(patientService, vaccineService);

        val inThePath = new Vaccine();
        inThePath.setId(0L);
        inThePath.setName("Vaccine");
        inThePath.setMaturityMonth(0);

        val nextOne1 = new Vaccine();
        nextOne1.setId(1L);
        nextOne1.setName("Vaccine");
        nextOne1.setMaturityMonth(1);

        val nextOne2 = new Vaccine();
        nextOne2.setId(11L);
        nextOne2.setName("Vaccine");
        nextOne2.setMaturityMonth(1);

        // Will be ignored - patient already vaccinated
        val nextOne3 = new Vaccine();
        nextOne3.setId(111L);
        nextOne3.setName("Vaccine");
        nextOne3.setMaturityMonth(1);

        val TooFarInTime = new Vaccine();
        TooFarInTime.setId(2L);
        TooFarInTime.setName("Vaccine");
        TooFarInTime.setMaturityMonth(5);

        Mockito.when(vaccineService.vaccines()).thenReturn(Arrays.asList(inThePath, nextOne1, TooFarInTime, nextOne2, nextOne3));

        val patient = new Patient();
        patient.setDob(LocalDate.now().minusDays(1).minusMonths(1));
        patient.setPatientVaccines(Collections.singleton(new PatientVaccine() {{
            setVaccine(nextOne3);
        }}));

        val nextVaccines = service.nextVaccines(patient);
        assertEquals(Arrays.asList(nextOne1, nextOne2), nextVaccines);
    }
}