package lu.formas.views.security.forms;

import lombok.val;
import lu.formas.Application;
import lu.formas.services.PatientService;
import lu.formas.services.UserService;
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
    UserService service;

    @MockitoBean
    AuthenticationManager authenticationManager;

    @Test
    public void invalid_Login_Should_Display_Field_Error_Messages_Only() {
        val form = new LoginForm(service, authenticationManager);

        assertEquals("Main error message should not be displayed", false, form.getErrorMessage().isVisible());
        assertEquals("Email should not display error message", null, form.getEmail().getErrorMessage());
        assertEquals("Password should not display error message", null, form.getPassword().getErrorMessage());

        form.getLogin().click();

        assertEquals("Main error message should not be displayed", false, form.getErrorMessage().isVisible());
        assertEquals("Email should display error message", "Email address is required", form.getEmail().getErrorMessage());
        assertEquals("Password should display error message", "Password is required", form.getPassword().getErrorMessage());
    }

    @Test
    public void invalid_Login_Should_Not_Display_Error_Messages_When_Auth_Is_Ok() {
        val form = new LoginForm(service, authenticationManager);
        form.getPassword().setValue("password");
        form.getEmail().setValue("email@email.com");

        val authenticated = UsernamePasswordAuthenticationToken.authenticated("", "", Collections.emptyList());

        when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenReturn(authenticated);

        assertEquals("Error message should not be visible", form.getErrorMessage().isVisible(), false);
        form.getLogin().click();
        assertEquals("Error message should be visible", form.getErrorMessage().isVisible(), false);
    }

    @Test
    public void invalid_Login_Should_Not_Display_Error_Messages_When_Auth_Failed() {
        val form = new LoginForm(service, authenticationManager);
        form.getPassword().setValue("password");
        form.getEmail().setValue("email@email.com");

        when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenReturn(null);

        assertEquals("Error message should not be visible", form.getErrorMessage().isVisible(), false);
        form.getLogin().click();
        assertEquals("Error message should be visible", form.getErrorMessage().isVisible(), true);
    }
}
