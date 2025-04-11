package lu.formas.forms.security;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import lombok.SneakyThrows;
import lombok.val;
import lu.formas.repository.model.Patient;
import lu.formas.services.PatientService;
import lu.formas.views.security.LoginView;

import java.util.Objects;
import java.util.stream.Stream;

public class PatientRegistrationForm extends FormLayout {
    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextField email = new TextField("Email");
    private PasswordField password = new PasswordField("Password");

    private Button save = new Button("Save");
    private Button close = new Button("Cancel");

    private Binder<Patient> binder = new BeanValidationBinder<>(Patient.class);
    private PatientService patientService;

    public PatientRegistrationForm(PatientService patientService) {
        this.patientService = patientService;

        val buttons = new HorizontalLayout(save, close);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        val civil = new HorizontalLayout(firstName, lastName);
        firstName.setSizeFull();
        lastName.setSizeFull();

        add(civil, email, password, buttons);

        setRequiredIndicatorVisible(firstName, lastName, email, password);

        binder.bindInstanceFields(this);

        save.addClickListener(this::save);
        close.addClickListener(this::close);

        setMaxWidth("500px");

        binder.forField(password).withValidator(this::passwordValidator).bind("password");
        binder.forField(email).withValidator(this::emailValidator).bind("email");

    }

    private ValidationResult passwordValidator(String password, ValueContext ctx) {
        /*
         * A real version should check for password
         * complexity as well
         */
        if (Objects.isNull(password) || password.length() < 8) {
            return ValidationResult.error("Password should be at least 8 characters long");
        }


        return ValidationResult.ok();
    }

    private ValidationResult emailValidator(String email, ValueContext ctx) {
        if (Objects.isNull(password)) {
            return ValidationResult.error("Email address is required");
        }

        val patient = new Patient();
        patient.setEmail(email);
        if (patientService.exists(patient)) {
            return ValidationResult.error("Email address is already registered");
        }

        return ValidationResult.ok();
    }

    private void setRequiredIndicatorVisible(HasValueAndElement<?, ?>... components) {
        Stream.of(components).forEach(comp -> comp.setRequiredIndicatorVisible(true));
    }

    @SneakyThrows
    private void save(ClickEvent<Button> event) {
        val patient = new Patient();
        binder.writeBeanIfValid(patient);

        if (binder.isValid()) {
            patientService.save(patient);
            close(event);
        }
    }

    private void close(ClickEvent<Button> event) {
        binder.setBean(null);
        close.getUI().ifPresent(ui ->
                ui.navigate(LoginView.LOGIN_ROUTE));
    }
}
