package lu.formas.services;

import lombok.SneakyThrows;
import lombok.val;
import lu.formas.Application;
import lu.formas.repository.PatientRepository;
import lu.formas.repository.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@AutoConfigureMockMvc
class NotificationServiceTest {
    @MockitoBean
    PatientService patientService;

    @Test
    @SneakyThrows
    public void test_Login_True_When_passwords_Match() {
        val service = new NotificationService(patientService);

        val patient = new Patient();

        patient.setDob(LocalDate.now().minusMonths(1));
        assertEquals(0, service.ageInMonths(patient)); //not quite just a month old yet

        patient.setDob(LocalDate.now().minusDays(1).minusMonths(1));
        assertEquals(1, service.ageInMonths(patient));

        patient.setDob(LocalDate.now().minusDays(1).minusMonths(12));
        assertEquals(12, service.ageInMonths(patient));
    }
}