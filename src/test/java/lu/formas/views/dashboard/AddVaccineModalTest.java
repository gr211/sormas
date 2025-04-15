package lu.formas.views.dashboard;

import lombok.val;
import lu.formas.Application;
import lu.formas.repository.model.Vaccine;
import lu.formas.services.PatientService;
import lu.formas.services.VaccineService;
import lu.formas.services.model.VaccinesByMaturity;
import lu.formas.services.model.VaccinesByMaturityKey;
import lu.formas.services.model.VaccinesByMaturityValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
    VaccineService vaccineService;

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

        val modal = new AddVaccineModal(patientService, vaccineService);

        val vaccines = modal.getSelect().getGenericDataView().getItems().collect(Collectors.toList());

        assertEquals("Unexpected vaccine", Arrays.asList(
                VaccinesByMaturityValue.placeholder(VaccinesByMaturityKey.month(1)),
                VaccinesByMaturityValue.of(vaccine1),
                VaccinesByMaturityValue.placeholder(VaccinesByMaturityKey.month(2)),
                VaccinesByMaturityValue.of(vaccine2)
        ), vaccines);
    }
}