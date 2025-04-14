package lu.formas.views.security;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lu.formas.services.PatientService;
import lu.formas.views.MainView;
import lu.formas.views.security.forms.LoginForm;
import org.springframework.security.authentication.AuthenticationManager;

import static com.vaadin.flow.component.Key.ENTER;

@Route(value = "login", layout = MainView.class)
@PageTitle("Login | Vaadin App")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    public static String LOGIN_ROUTE = "login";

    public final LoginForm login;

    private final PatientService patientService;
    private final AuthenticationManager authenticationManager;

    public LoginView(PatientService patientService, AuthenticationManager authenticationManager) {
        this.patientService = patientService;
        this.authenticationManager = authenticationManager;

        login = new LoginForm(patientService, authenticationManager);

        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setHorizontalComponentAlignment(Alignment.CENTER, login);

        // Configure form behavior for enterkey
        login.getPassword().addKeyPressListener(ENTER, e -> submitForm());
        login.getLogin().addClickListener(e -> submitForm());

        // Create title
        H1 title = new H1("User Management App");
        title.getStyle().set("margin-bottom", "var(--lumo-space-m)");

        // Optional: Add a logo
        Image logo = new Image("frontend/images/logo.png", "App Logo");
        logo.setHeight("64px");
        logo.setWidth("auto");
        logo.getStyle().set("margin-bottom", "var(--lumo-space-l)");

        // Add components to layout
        add(title, login);
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