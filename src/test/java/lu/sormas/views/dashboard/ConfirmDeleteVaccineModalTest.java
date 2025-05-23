package lu.sormas.views.dashboard;

import lombok.val;
import lu.sormas.Application;
import lu.sormas.repository.model.PatientVaccine;
import lu.sormas.repository.model.Vaccine;
import lu.sormas.services.NotificationService;
import lu.sormas.services.PatientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@AutoConfigureMockMvc
class ConfirmDeleteVaccineModalTest {

    @MockitoBean
    PatientService patientService;

    @MockitoBean
    VaccinationHistoryGrid vaccinationHistoryGrid;

    @MockitoBean
    VaccineNotifications vaccineNotifications;

    @Test
    public void ensure_items_are_loaded_in_order() {
        val vaccine1 = new Vaccine() {{
            setName("Vaccine 1");
            setMaturityMonth(1);
        }};

        val patientVaccine = new PatientVaccine() {{
            setVaccine(vaccine1);
        }};

        Mockito.doNothing().when(vaccinationHistoryGrid).refresh();

        val modal = new ConfirmDeleteVaccineModal(vaccinationHistoryGrid, patientService, patientVaccine, vaccineNotifications, "email");
        modal.prepareModal();

        modal.getConfirmButton().click();

        Mockito.verify(patientService).removeVaccination(Mockito.argThat(e -> {
            assertEquals("Wrong email", "email", e);
            return true;
        }), Mockito.argThat(e -> {
            ;
            assertEquals("Wrong patient vaccine", patientVaccine, e);
            return true;
        }));

        Mockito.verify(vaccinationHistoryGrid).refresh();
    }
}