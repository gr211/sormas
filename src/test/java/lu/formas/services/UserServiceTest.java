package lu.formas.services;

import lombok.SneakyThrows;
import lombok.val;
import lu.formas.Application;
import lu.formas.repository.PatientRepository;
import lu.formas.repository.UserRepository;
import lu.formas.repository.model.Login;
import lu.formas.repository.model.User;
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
class UserServiceTest {

    @MockitoBean
    UserRepository userRepository;

    @MockitoBean
    PatientRepository patientRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @SneakyThrows
    public void test_Login_True_When_passwords_Match() {
        val service = new UserService(userRepository, patientRepository, passwordEncoder);

        val password = passwordEncoder.encode("password");

        val user = new User();
        user.setEmail("email@email.com");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPassword(password);

        Mockito.when(userRepository.findByEmail(Mockito.eq("email@email.com"))).thenReturn(Optional.of(user));

        val login = new Login();
        login.setPassword("password");
        login.setEmail("email@email.com");
        val result = service.login(login);
        assert result;

        Mockito.verify(userRepository).findByEmail(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    @SneakyThrows
    public void test_Login_False_When_passwords_Dont_Match() {
        val service = new UserService(userRepository, patientRepository, passwordEncoder);

        val password = passwordEncoder.encode("password");

        val user = new User();
        user.setEmail("email@email.com");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPassword(password);

        Mockito.when(userRepository.findByEmail(Mockito.eq("email@email.com"))).thenReturn(Optional.of(user));

        val login = new Login();
        login.setPassword("different-password");
        login.setEmail("email@email.com");
        val result = service.login(login);
        assert !result;

        Mockito.verify(userRepository).findByEmail(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    @SneakyThrows
    public void test_Delete_Patient_From_Database() {
        val service = new UserService(userRepository, patientRepository, passwordEncoder);

        val user = new User();
        user.setEmail("email@email.com");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPassword("password");

        Mockito.doNothing().when(userRepository).deleteUserByEmail(Mockito.anyString());
        Mockito.doNothing().when(patientRepository).deletePatientByEmail(Mockito.anyString());

        service.delete(user);

        Mockito.verify(userRepository).deleteUserByEmail(Mockito.eq("email@email.com"));
        Mockito.verify(patientRepository).deletePatientByEmail(Mockito.eq("email@email.com"));
        Mockito.verifyNoMoreInteractions(userRepository);
    }
}