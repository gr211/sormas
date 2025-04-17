package lu.formas.views.dashboard;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.val;
import lu.formas.security.SecurityService;
import lu.formas.services.NotificationService;
import lu.formas.services.PatientService;
import lu.formas.services.VaccineService;

public class VaccinationHistoryView extends VerticalLayout {

    private final AddVaccineModal addVaccineModal;

    public VaccinationHistoryView(VaccinationHistoryGrid vaccinationHistoryGrid, PatientService patientService, VaccineService vaccineService, NotificationService notificationService, SecurityService securityService) {
        setClassName("vaccination-history-view-layout");
        setJustifyContentMode(JustifyContentMode.START);
        setAlignItems(Alignment.START);

        val email = securityService.getAuthenticatedUser().getUsername();
        val patient = patientService.byEmail(email).get();

        val vaccineNotifications = new VaccineNotifications(notificationService, patient);
        addVaccineModal = new AddVaccineModal(vaccinationHistoryGrid, patientService, vaccineService, securityService, vaccineNotifications);

        val title = new H1("Vaccination History");

        val addVaccine = new Button("Add vaccine", event -> {
            addVaccineModal.open();
        });

        add(title, vaccineNotifications, addVaccine);
    }
}