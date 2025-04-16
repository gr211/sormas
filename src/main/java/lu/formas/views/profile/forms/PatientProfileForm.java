package lu.formas.views.profile.forms;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import lombok.SneakyThrows;
import lombok.val;
import lu.formas.repository.model.User;
import lu.formas.security.SecurityService;
import lu.formas.services.UserService;

public class PatientProfileForm extends FormLayout {
    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextField email = new TextField("Email");

    private Button remove = new Button("Remove account");

    private Binder<User> binder = new BeanValidationBinder<>(User.class);
    private UserService userService;
    private SecurityService securityService;

    private final ConfirmDelete confirmDelete;

    public PatientProfileForm(UserService userService, SecurityService securityService) {
        fillUserInfo(userService, securityService);
        binder.bindInstanceFields(this);

        this.userService = userService;
        this.securityService = securityService;
        this.confirmDelete = modal();

        val buttons = new HorizontalLayout(remove);
        remove.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        firstName.setRequired(false);
        lastName.setRequired(false);
        email.setRequired(false);

        val civil = new HorizontalLayout(firstName, lastName);
        firstName.setSizeFull();
        lastName.setSizeFull();

        add(civil, email, buttons);

        remove.addClickListener(this::openModal);
    }

    private ConfirmDelete modal() {
        return new ConfirmDelete(binder.getBean(), userService, securityService);
    }

    @SneakyThrows
    private void openModal(ClickEvent<Button> event) {
        confirmDelete.open();
    }

    public void fillUserInfo(UserService userService, SecurityService securityService) {
        val userDetails = securityService.getAuthenticatedUser();

        userService.get(userDetails).ifPresent(p -> {
            p.setPassword(null);
            binder.setBean(p);

            email.setRequired(false);
            email.setReadOnly(true);

            firstName.setRequired(false);
            firstName.setReadOnly(true);

            lastName.setRequired(false);
            lastName.setReadOnly(true);
        });
    }
}
