package lu.formas.views.security.forms;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import lu.formas.repository.model.User;
import lu.formas.services.UserService;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Stream;

public class PatientRegistrationForm extends FormLayout {
    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextField email = new TextField("Email");
    private PasswordField password = new PasswordField("Password");
    private DatePicker dob = new DatePicker("Date of birth");

    private Button save = new Button("Save");
    private Button close = new Button("Cancel");

    private Binder<User> binder = new BeanValidationBinder<>(User.class);
    private UserService userService;

    public PatientRegistrationForm(UserService patientService) {
        this.userService = patientService;

        val buttons = new HorizontalLayout(save, close);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        val civil = new HorizontalLayout(firstName, lastName);
        firstName.setSizeFull();
        lastName.setSizeFull();

        add(civil, dob, email, password, buttons);

        setRequiredIndicatorVisible(firstName, lastName, email, password);

        binder.bindInstanceFields(this);

        save.addClickListener(this::save);
        close.addClickListener(this::close);

        binder.forField(password).withValidator(this::passwordValidator).bind("password");
        binder.forField(email).withValidator(this::emailValidator).bind("email");
        binder.forField(dob).withValidator(this::dobValidator).bind("dob");
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

    private ValidationResult dobValidator(LocalDate date, ValueContext ctx) {
        if (Objects.isNull(date)) {
            return ValidationResult.error("Date of birth is required");
        }

        if (LocalDate.now().plusDays(1).isBefore(date)) {
            return ValidationResult.error("Date of birth should be in the past");
        }

        return ValidationResult.ok();
    }

    private ValidationResult emailValidator(String email, ValueContext ctx) {
        if (Objects.isNull(password)) {
            return ValidationResult.error("Email address is required");
        }

        val user = new User();
        user.setEmail(email);
        if (userService.exists(user)) {
            return ValidationResult.error("Email address is already registered");
        }

        return ValidationResult.ok();
    }

    private void setRequiredIndicatorVisible(HasValueAndElement<?, ?>... components) {
        Stream.of(components).forEach(comp -> comp.setRequiredIndicatorVisible(true));
    }

    @SneakyThrows
    private void save(ClickEvent<Button> event) {
        val user = new User();
        binder.writeBeanIfValid(user);

        if (binder.isValid()) {
            userService.save(user);
            close(event);
        }
    }

    private void close(ClickEvent<Button> event) {
        binder.setBean(null);
        close.getUI().ifPresent(ui ->
                ui.navigate("login"));
    }
}
