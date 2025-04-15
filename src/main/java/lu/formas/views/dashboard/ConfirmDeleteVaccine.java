package lu.formas.views.dashboard;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.val;
import lu.formas.repository.model.PatientVaccine;
import lu.formas.repository.model.User;
import lu.formas.security.SecurityService;
import lu.formas.services.PatientService;
import lu.formas.services.UserService;

public class ConfirmDeleteVaccine extends Dialog {
    public ConfirmDeleteVaccine(PatientService patientService, PatientVaccine patientVaccine) {
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        setClassName("confirm-delete");

        val dialogLayout = new VerticalLayout();
        val message = new H2("Delete this entry?");
        val name = new Div(patientVaccine.getVaccine().getName());
        dialogLayout.add(message, name);

        val buttonsLayout = new HorizontalLayout();
        val confirmButton = new Button("Confirm", event -> {

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