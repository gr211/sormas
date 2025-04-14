package lu.formas.views.profile.forms;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.val;
import lu.formas.repository.model.Patient;
import lu.formas.security.SecurityService;
import lu.formas.services.PatientService;

public class ConfirmDelete extends Dialog {
    public ConfirmDelete(Patient patient, PatientService patientService, SecurityService securityService) {
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        setWidth("400px");
        setHeight("200px");


        val dialogLayout = new VerticalLayout();
        val message = new H2("Are you sure you want to proceed?");
        dialogLayout.add(message);

        val buttonsLayout = new HorizontalLayout();
        val confirmButton = new Button("Confirm", event -> {
            patientService.delete(patient);
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