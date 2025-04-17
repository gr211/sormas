package lu.formas.views.profile.forms;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.val;
import lu.formas.repository.model.User;
import lu.formas.security.SecurityService;
import lu.formas.services.UserService;

public class ConfirmDelete extends Dialog {
    public ConfirmDelete(User user, UserService userService, SecurityService securityService) {
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        setClassName("confirm-delete");

        val dialogLayout = new VerticalLayout();
        val message = new H2("Are you sure you want to proceed?");
        dialogLayout.add(message);

        val buttonsLayout = new HorizontalLayout();
        val confirmButton = new Button("Confirm", event -> {
            userService.delete(user);
            securityService.logout();

            close();
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        val cancelButton = new Button("Cancel", event -> {
            Notification.show("Operation cancelled");
            close();
        });
        buttonsLayout.add(confirmButton, cancelButton);


        dialogLayout.add(buttonsLayout);
        add(dialogLayout);
    }
}