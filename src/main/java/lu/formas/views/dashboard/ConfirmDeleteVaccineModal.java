package lu.formas.views.dashboard;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;
import lombok.val;
import lu.formas.repository.model.PatientVaccine;
import lu.formas.services.NotificationService;
import lu.formas.services.PatientService;

@Getter
public class ConfirmDeleteVaccineModal extends Dialog {

    private Button confirmButton;

    private PatientService patientService;
    private PatientVaccine patientVaccine;
    private VaccinationHistoryGrid vaccinationHistoryGrid;
    private VaccineNotifications vaccineNotifications;
    private String email;

    public ConfirmDeleteVaccineModal(VaccinationHistoryGrid vaccinationHistoryGrid, PatientService patientService, PatientVaccine patientVaccine, VaccineNotifications vaccineNotifications, String email) {
        this.patientService = patientService;
        this.patientVaccine = patientVaccine;
        this.vaccinationHistoryGrid = vaccinationHistoryGrid;
        this.vaccineNotifications = vaccineNotifications;
        this.email = email;

        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        setClassName("confirm-delete");

        prepareModal();
    }

    public void prepareModal() {
        val dialogLayout = new VerticalLayout();
        val message = new H2("Delete this entry?");
        val name = new Div(patientVaccine.getVaccine().getName());
        dialogLayout.add(message, name);

        val buttonsLayout = new HorizontalLayout();

        confirmButton = new Button("Confirm", event -> {
            patientService.removeVaccination(email, patientVaccine);
            vaccinationHistoryGrid.refresh();
            vaccineNotifications.refresh();
            close();
        });

        val cancelButton = new Button("Cancel", event -> {
            Notification.show("Operation cancelled");
            close();
        });
        buttonsLayout.add(confirmButton, cancelButton);

        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        dialogLayout.add(buttonsLayout);
        add(dialogLayout);
    }
}