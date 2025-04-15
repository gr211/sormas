package lu.formas.views.dashboard;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.val;
import lu.formas.services.PatientService;
import lu.formas.services.VaccineService;

public class VaccinationHistoryView extends VerticalLayout {

    private final AddVaccineModal addVaccineModal;

    public VaccinationHistoryView(PatientService patientService, VaccineService vaccineService) {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.START);
        setAlignItems(Alignment.START);

        addVaccineModal = new AddVaccineModal(patientService, vaccineService);

        val title = new H1("Vaccination Card");

        val addVaccine = new Button("Add vaccine", event -> {
            addVaccineModal.open();
        });

        add(title, addVaccine);
    }
}