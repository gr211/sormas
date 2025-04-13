package lu.formas.security;

import lombok.SneakyThrows;
import lombok.val;
import lu.formas.Application;
import lu.formas.repository.PatientRepository;
import lu.formas.repository.model.Login;
import lu.formas.repository.model.Patient;
import lu.formas.services.PatientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@AutoConfigureMockMvc
class PatientServiceTest {

    @MockitoBean
    PatientRepository patientRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @SneakyThrows
    public void test_Login_True_When_passwords_Match() {
        val service = new PatientService(patientRepository, passwordEncoder);

        val password = passwordEncoder.encode("password");

        val patient = new Patient();
        patient.setEmail("email@email.com");
        patient.setFirstName("firstName");
        patient.setLastName("lastName");
        patient.setPassword(password);

        Mockito.when(patientRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(patient));

        val login = new Login();
        login.setPassword("password");
        login.setEmail("email@email.com");
        val result = service.login(login);
        assert result;
    }

    @Test
    @SneakyThrows
    public void test_Login_False_When_passwords_Dont_Match() {
        val service = new PatientService(patientRepository, passwordEncoder);

        val password = passwordEncoder.encode("password");

        val patient = new Patient();
        patient.setEmail("email@email.com");
        patient.setFirstName("firstName");
        patient.setLastName("lastName");
        patient.setPassword(password);

        Mockito.when(patientRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(patient));

        val login = new Login();
        login.setPassword("different-password");
        login.setEmail("email@email.com");
        val result = service.login(login);
        assert !result;
    }
}