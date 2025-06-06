package lu.sormas.views.dashboard;

import lombok.val;
import lu.sormas.Application;
import lu.sormas.repository.model.Patient;
import lu.sormas.repository.model.Vaccine;
import lu.sormas.security.SecurityService;
import lu.sormas.services.NotificationService;
import lu.sormas.services.PatientService;
import lu.sormas.services.VaccineService;
import lu.sormas.services.model.VaccinesByMaturity;
import lu.sormas.services.model.VaccinesByMaturityKey;
import lu.sormas.services.model.VaccinesByMaturityValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@AutoConfigureMockMvc
class AddVaccineModalTest {

    @MockitoBean
    PatientService patientService;

    @MockitoBean
    SecurityService securityService;

    @MockitoBean
    VaccineService vaccineService;

    @MockitoBean
    VaccineNotifications vaccineNotifications;

    @Test
    public void ensure_items_are_loaded_in_order() {
        val vaccine1 = new Vaccine() {{
            setName("Vaccine 1");
            setMaturityMonth(1);
        }};
        val vaccine2 = new Vaccine() {{
            setName("Vaccine 2");
            setMaturityMonth(2);
        }};

        val vaccineByMaturity = new VaccinesByMaturity(Arrays.asList(vaccine1, vaccine2));
        Mockito.when(vaccineService.groupedByMaturity()).thenReturn(vaccineByMaturity);

        val userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(securityService.getAuthenticatedUser()).thenReturn(userDetails);
        Mockito.when(userDetails.getUsername()).thenReturn("username");

        val vaccinationHistoryGrid = new VaccinationHistoryGrid(patientService, securityService, vaccineNotifications);

        val patient = new Patient();
        patient.setDob(LocalDate.of(2025, 1, 1));

        val modal = new AddVaccineModal(vaccinationHistoryGrid, patientService, vaccineService, securityService, vaccineNotifications);

        val vaccines = modal.getSelect().getGenericDataView().getItems().collect(Collectors.toList());

        assertEquals("Unexpected vaccine", Arrays.asList(
                VaccinesByMaturityValue.placeholder(VaccinesByMaturityKey.month(1)),
                VaccinesByMaturityValue.of(vaccine1),
                VaccinesByMaturityValue.placeholder(VaccinesByMaturityKey.month(2)),
                VaccinesByMaturityValue.of(vaccine2)
        ), vaccines);
    }
}