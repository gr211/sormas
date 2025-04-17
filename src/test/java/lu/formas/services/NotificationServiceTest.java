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
import java.util.Optional;

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

        val inThePast = new Vaccine();
        inThePast.setId(0L);
        inThePast.setName("Vaccine");
        inThePast.setMaturityMonth(0);

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

        val tooFarInTime = new Vaccine();
        tooFarInTime.setId(2L);
        tooFarInTime.setName("Vaccine");
        tooFarInTime.setMaturityMonth(5);

        Mockito.when(vaccineService.vaccines()).thenReturn(Arrays.asList(inThePast, nextOne1, tooFarInTime, nextOne2, nextOne3));

        val patient = new Patient();
        patient.setDob(LocalDate.now().minusDays(1).minusMonths(1));
        patient.setPatientVaccines(Collections.singleton(new PatientVaccine() {{
            setVaccine(nextOne3);
        }}));

        val nextVaccines = service.nextVaccines(patient);
        assertEquals(Arrays.asList(nextOne1, nextOne2), nextVaccines);
    }

    @Test
    @SneakyThrows
    public void test_Overdue_Vaccine() {
        val service = new NotificationService(patientService, vaccineService);

        val overdue1 = new Vaccine();
        overdue1.setId(0L);
        overdue1.setName("Vaccine");
        overdue1.setMaturityMonth(0);

        val overdue2 = new Vaccine();
        overdue2.setId(1L);
        overdue2.setName("Vaccine");
        overdue2.setMaturityMonth(1);

        val overdue3 = new Vaccine();
        overdue3.setId(3L);
        overdue3.setName("Vaccine");
        overdue3.setMaturityMonth(1);

        val alreadyVaccinated = new Vaccine();
        alreadyVaccinated.setId(4L);
        alreadyVaccinated.setName("Vaccine");
        alreadyVaccinated.setMaturityMonth(4);

        val overdue4 = new Vaccine();
        overdue4.setId(5L);
        overdue4.setName("Vaccine");
        overdue4.setMaturityMonth(5);

        val notYetDue = new Vaccine();
        notYetDue.setId(6L);
        notYetDue.setName("Vaccine");
        notYetDue.setMaturityMonth(15);

        Mockito.when(vaccineService.vaccines()).thenReturn(Arrays.asList(overdue1, overdue2, overdue3, alreadyVaccinated, overdue4, notYetDue));

        val oneYearOldPatient = new Patient();
        oneYearOldPatient.setDob(LocalDate.now().minusMonths(12));
        oneYearOldPatient.setPatientVaccines(Collections.singleton(new PatientVaccine() {{
            setVaccine(alreadyVaccinated);
        }}));

        val overdueVaccines = service.overdueVaccines(oneYearOldPatient);
        assertEquals(Arrays.asList(overdue1, overdue2, overdue3, overdue4), overdueVaccines);
    }

    @Test
    @SneakyThrows
    public void test_Notifications_By_Patient() {

        val oneYearOldPatient = new Patient();
        oneYearOldPatient.setDob(LocalDate.now().minusMonths(12));
        oneYearOldPatient.setEmail("email");

        val service = new NotificationService(patientService, vaccineService);

        val overdue = new Vaccine();
        overdue.setId(0L);
        overdue.setName("Vaccine");
        overdue.setMaturityMonth(0);

        val next = new Vaccine();
        next.setId(1L);
        next.setName("Vaccine");
        next.setMaturityMonth(15);

        Mockito.when(vaccineService.vaccines()).thenReturn(Arrays.asList(overdue, next));

        val notifications = service.notifications(oneYearOldPatient);
        assertEquals(Collections.singletonList(next), notifications.getNextVaccines());
        assertEquals(Collections.singletonList(overdue), notifications.getOverdueVaccines());

        Mockito.verify(vaccineService, Mockito.times(2)).vaccines();
        Mockito.verifyNoMoreInteractions(vaccineService);
    }

    @Test
    @SneakyThrows
    public void test_Notifications_By_Email() {

        val oneYearOldPatient = new Patient();
        oneYearOldPatient.setDob(LocalDate.now().minusMonths(12));
        oneYearOldPatient.setEmail("email");

        val service = new NotificationService(patientService, vaccineService);

        val overdue = new Vaccine();
        overdue.setId(0L);
        overdue.setName("Vaccine");
        overdue.setMaturityMonth(0);

        val next = new Vaccine();
        next.setId(1L);
        next.setName("Vaccine");
        next.setMaturityMonth(15);

        Mockito.when(vaccineService.vaccines()).thenReturn(Arrays.asList(overdue, next));
        Mockito.when(patientService.byEmail(Mockito.anyString())).thenReturn(Optional.of(oneYearOldPatient));

        val notifications = service.notifications(oneYearOldPatient.getEmail());
        assertEquals(Collections.singletonList(next), notifications.getNextVaccines());
        assertEquals(Collections.singletonList(overdue), notifications.getOverdueVaccines());

        Mockito.verify(patientService).byEmail(Mockito.eq("email"));
        Mockito.verify(vaccineService, Mockito.times(2)).vaccines();
        Mockito.verifyNoMoreInteractions(vaccineService);
    }
}