package lu.sormas.views.dashboard;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.val;
import lu.sormas.repository.model.Patient;
import lu.sormas.repository.model.User;
import lu.sormas.security.SecurityService;
import lu.sormas.services.PatientService;
import lu.sormas.services.VaccineService;

public class VaccinationHistoryView extends VerticalLayout {

    public VaccinationHistoryView(Patient patient, User user, VaccinationHistoryGrid vaccinationHistoryGrid, PatientService patientService, VaccineService vaccineService, VaccineNotifications vaccineNotifications, SecurityService securityService) {
        setClassName("vaccination-history-view-layout");
        setJustifyContentMode(JustifyContentMode.START);
        setAlignItems(Alignment.START);

        val title = new H1( user.getFirstName() + " " + user.getLastName() + " - vaccination history");

        val addVaccine = new Button("Add vaccine", event -> {
            new AddVaccineModal(vaccinationHistoryGrid, patientService, vaccineService, securityService, vaccineNotifications).open();
        });

        add(title, vaccineNotifications, addVaccine);
    }
}