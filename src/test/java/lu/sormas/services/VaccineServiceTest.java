package lu.sormas.services;

import lombok.SneakyThrows;
import lombok.val;
import lu.sormas.Application;
import lu.sormas.repository.VaccineRepository;
import lu.sormas.repository.model.Vaccine;
import lu.sormas.services.model.VaccinesByMaturityKey;
import lu.sormas.services.model.VaccinesByMaturityValue;
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
    VaccineRepository vaccinRepository;

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

        assertEquals("Unexpected vaccine", VaccinesByMaturityValue.of(vaccine1), vaccines.getEntries().get(new VaccinesByMaturityKey(1)).get(0));
        assertEquals("Unexpected vaccine", VaccinesByMaturityValue.of(vaccine2), vaccines.getEntries().get(new VaccinesByMaturityKey(2)).get(0));
    }

    @Test
    @SneakyThrows
    public void test_flatten_should_order_vaccines_for_dropdown() {
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

        val vaccines = service.groupedByMaturity().flatten();

        assertEquals("Unexpected vaccine", Arrays.asList(
                VaccinesByMaturityValue.placeholder(VaccinesByMaturityKey.month(1)),
                VaccinesByMaturityValue.of(vaccine1),
                VaccinesByMaturityValue.placeholder(VaccinesByMaturityKey.month(2)),
                VaccinesByMaturityValue.of(vaccine2)
        ), vaccines);
    }

    @Test
    @SneakyThrows
    public void test_getMaturityKey() {
        assertEquals("Unexpected key", 2, new VaccinesByMaturityKey(2).getMaturityKey());
        assertEquals("Unexpected name", "2 months", new VaccinesByMaturityKey(2).getMaturityName());
    }

}