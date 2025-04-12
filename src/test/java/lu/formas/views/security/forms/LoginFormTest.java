package lu.formas.views.security.forms;

import lombok.val;
import lu.formas.Application;
import lu.formas.services.PatientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@AutoConfigureMockMvc
public class LoginFormTest {

    @MockitoBean
    PatientService service;

    @MockitoBean
    AuthenticationManager authenticationManager;

    @Test
    public void invalid_Login_Should_Display_Error_Message() {
        val form = new LoginForm(service, authenticationManager);

        assertEquals("", form.getErrorMessage().isVisible(), false);
        form.getLogin().click();
        assertEquals("", form.getErrorMessage().isVisible(), true);
    }

    @Test
    public void invalid_Login_Should_Display_Error_MessagesXXX() {
        val form = new LoginForm(service, authenticationManager);

        val authenticated = UsernamePasswordAuthenticationToken.authenticated("", "", Collections.emptyList());

        when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenReturn(authenticated);

        assertEquals("", form.getErrorMessage().isVisible(), false);
        form.getLogin().click();
        assertEquals("", form.getErrorMessage().isVisible(), false);
    }
}
