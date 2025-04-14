package lu.formas.security;

import lombok.SneakyThrows;
import lombok.val;
import lu.formas.Application;
import lu.formas.repository.PatientRepository;
import lu.formas.repository.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@AutoConfigureMockMvc
class PatientDetailsServiceTest {

    @MockitoBean
    PatientRepository patientRepository;

    @Autowired
    PatientDetailsService patientDetailsService;


    @Test
    @SneakyThrows
    public void test_LoadUserByUsername() {
        val patient = new Patient();
        patient.setEmail("email@email.com");
        patient.setFirstName("firstName");
        patient.setLastName("lastName");
        patient.setPassword("password");

        Mockito.when(patientRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(patient));

        val user = patientDetailsService.loadUserByUsername("email@email.com");

        assert user != null;
        assert user.getUsername().equals("email@email.com");
        assert user.getPassword().equals("password");
        assert user.getAuthorities().size() == 1;
        assert user.getAuthorities().stream().findFirst().get().getAuthority().equals("ROLE_USER");

        Mockito.verify(patientRepository).findByEmail(Mockito.eq("email@email.com"));
        Mockito.verifyNoMoreInteractions(patientRepository);
    }
}