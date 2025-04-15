package lu.formas.services;

import lombok.SneakyThrows;
import lombok.val;
import lu.formas.Application;
import lu.formas.repository.VaccinRepository;
import lu.formas.repository.model.Vaccine;
import lu.formas.services.model.VaccinesByMaturityKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@AutoConfigureMockMvc
class VaccineServiceTest {

    @MockitoBean
    VaccinRepository vaccinRepository;

    @Test
    @SneakyThrows
    public void test_groupedByMaturity() {
        val service = new VaccineService(vaccinRepository);

        val vaccine1 = new Vaccine() {{
            setName("Vaccine 1");
            setMaturityMonth(1);
        }};
        val vaccine2 = new Vaccine() {{
            setName("Vaccine 2");
            setMaturityMonth(2);
        }};

        Mockito.when(vaccinRepository.findAll(Mockito.any(Sort.class))).thenReturn(Arrays.asList(vaccine1, vaccine2));

        val vaccines = service.groupedByMaturity();
        assertEquals("There should be 2 vaccines", 2, vaccines.getEntries().size());

        assertEquals("Unexpected vaccine", vaccine1, vaccines.getEntries().get(new VaccinesByMaturityKey(1)).get(0));
        assertEquals("Unexpected vaccine", vaccine2, vaccines.getEntries().get(new VaccinesByMaturityKey(2)).get(0));
    }

    @Test
    @SneakyThrows
    public void test_getMaturityKey() {
        assertEquals("Unexpected key", 2, new VaccinesByMaturityKey(2).getMaturityKey());
        assertEquals("Unexpected name", "2 months", new VaccinesByMaturityKey(2).getMaturityName());
    }

}