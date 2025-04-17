package lu.sormas.views.security;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.val;
import lu.sormas.services.PatientService;
import lu.sormas.services.UserService;
import lu.sormas.views.MainView;
import lu.sormas.views.common.SormasLogo;
import lu.sormas.views.security.forms.LoginForm;
import org.springframework.security.authentication.AuthenticationManager;

import static com.vaadin.flow.component.Key.ENTER;

@Route(value = "login", layout = MainView.class)
@PageTitle("Login | Vaadin App")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    public final LoginForm login;

    public LoginView(UserService userService, AuthenticationManager authenticationManager) {

        login = new LoginForm(userService, authenticationManager);

        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setHorizontalComponentAlignment(Alignment.CENTER, login);

        login.getPassword().addKeyPressListener(ENTER, e -> submitForm());
        login.getLogin().addClickListener(e -> submitForm());

        val title = new H1("Vaccination Management App");

        val p = new Paragraph(new RouterLink("New ? Register here", RegistrationView.class));

        // Add components to layout
        add(new SormasLogo(), title, login, p);
    }

    private void submitForm() {
        login.login(null);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // Check if error parameter exists in URL
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {

            // Show error message
            login.setError(true);
        }
    }
}