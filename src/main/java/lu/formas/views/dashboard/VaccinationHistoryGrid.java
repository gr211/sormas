package lu.formas.views.dashboard;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.val;
import lu.formas.repository.model.PatientVaccine;
import lu.formas.security.SecurityService;
import lu.formas.services.PatientService;
import lu.formas.services.VaccineService;

import java.util.Objects;

public class VaccinationHistoryGrid extends VerticalLayout {

    Grid<PatientVaccine> grid = new Grid<>(PatientVaccine.class, false);

    public VaccinationHistoryGrid(PatientService patientService, VaccineService vaccineService, SecurityService securityService) {
        setClassName("vaccination-history-grid");
        setJustifyContentMode(JustifyContentMode.START);
        setAlignItems(Alignment.START);

        val title = new H1("Vaccination History");

        val authenticatedUser = securityService.getAuthenticatedUser();

        if (Objects.nonNull(authenticatedUser)) {
            grid.addColumn(PatientVaccine::getVaccineDate).setHeader("Date");
            grid.addColumn(v -> v.getVaccine().getName()).setHeader("Vaccine");
            grid.addColumn(PatientVaccine::getComments).setHeader("Observations");

            grid.setItems(patientService.getVaccinesEntries(authenticatedUser.getUsername()));
        }


        add(title, grid);
    }
}