package lu.sormas.security;

import lombok.SneakyThrows;
import lombok.val;
import lu.sormas.Application;
import lu.sormas.repository.UserRepository;
import lu.sormas.repository.model.Patient;
import lu.sormas.repository.model.User;
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
class UserDetailsServiceTest {

    @MockitoBean
    UserRepository userRepository;

    @Autowired
    SormasUserDetailsService sormasUserDetailsService;


    @Test
    @SneakyThrows
    public void test_LoadUserByUsername() {
        val expected = new User();
        expected.setEmail("email@email.com");
        expected.setFirstName("firstName");
        expected.setLastName("lastName");
        expected.setPassword("password");

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(expected));

        val user = sormasUserDetailsService.loadUserByUsername("email@email.com");

        assert user != null;
        assert user.getUsername().equals("email@email.com");
        assert user.getPassword().equals("password");
        assert user.getAuthorities().size() == 1;
        assert user.getAuthorities().stream().findFirst().get().getAuthority().equals("ROLE_USER");

        Mockito.verify(userRepository).findByEmail(Mockito.eq("email@email.com"));
        Mockito.verifyNoMoreInteractions(userRepository);
    }
}