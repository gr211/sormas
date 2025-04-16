package lu.formas.views.security.forms;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import lombok.Getter;
import lombok.val;
import lu.formas.repository.model.Login;
import lu.formas.services.PatientService;
import lu.formas.services.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
public class LoginForm extends FormLayout {
    private TextField email = new TextField("Email");
    private PasswordField password = new PasswordField("Password");
    private Div errorMessage = new Div();
    private Button login = new Button("Sign in");

    private Binder<Login> binder = new BeanValidationBinder<>(Login.class);

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public LoginForm(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;

        val buttons = new HorizontalLayout(login);
        login.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add(errorMessage, email, password, buttons);

        email.setPrefixComponent(new Icon(VaadinIcon.USER));

        password.setPrefixComponent(new Icon(VaadinIcon.LOCK));

        setRequiredIndicatorVisible(email, password);

        binder.bindInstanceFields(this);

        login.addClickListener(this::login);

        configureErrorMessage();
    }

    private void configureErrorMessage() {
        errorMessage.setClassName("error-message");
        errorMessage.setVisible(false);
        errorMessage.setText("Invalid username or password");
    }

    private void setRequiredIndicatorVisible(HasValueAndElement<?, ?>... components) {
        Stream.of(components).forEach(comp -> comp.setRequiredIndicatorVisible(true));
    }

    public void setError(boolean error) {
        errorMessage.setVisible(error);
    }

    public void login(ClickEvent<Button> event) {
        errorMessage.setVisible(false);
        val login = new Login();
        binder.writeBeanIfValid(login);

        if (binder.isValid()) {
            val authentication = new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword());
            try {
                val authenticate = authenticationManager.authenticate(authentication);

                if (Objects.isNull(authenticate)) {
                    errorMessage.setVisible(true);
                    return;
                }

                SecurityContextHolder.getContext().setAuthentication(authenticate);

                getUI().ifPresent(ui -> ui.navigate("dashboard"));
            } catch (Exception e) {
                setError(true);
            }
        }
    }
}
