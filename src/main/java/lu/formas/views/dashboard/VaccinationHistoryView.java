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

    private final AddVaccineModal addVaccineModal;

    public VaccinationHistoryView(Patient patient, User user, VaccinationHistoryGrid vaccinationHistoryGrid, PatientService patientService, VaccineService vaccineService, VaccineNotifications vaccineNotifications, SecurityService securityService) {
        setClassName("vaccination-history-view-layout");
        setJustifyContentMode(JustifyContentMode.START);
        setAlignItems(Alignment.START);


        addVaccineModal = new AddVaccineModal(patient, vaccinationHistoryGrid, patientService, vaccineService, securityService, vaccineNotifications);

        val title = new H1( user.getFirstName() + " " + user.getLastName() + " - vaccination history");

        val addVaccine = new Button("Add vaccine", event -> {
            addVaccineModal.open();
        });

        add(title, vaccineNotifications, addVaccine);
    }
}