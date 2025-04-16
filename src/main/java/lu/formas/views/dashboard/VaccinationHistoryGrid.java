package lu.formas.views.dashboard;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.val;
import lu.formas.repository.model.PatientVaccine;
import lu.formas.security.SecurityService;
import lu.formas.services.PatientService;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class VaccinationHistoryGrid extends VerticalLayout {

    Grid<PatientVaccine> grid = new Grid<>(PatientVaccine.class, false);
    TextField searchField = new TextField();

    public VaccinationHistoryGrid(PatientService patientService, SecurityService securityService) {
        setClassName("vaccination-history-grid");
        setJustifyContentMode(JustifyContentMode.START);
        setAlignItems(Alignment.START);

        val title = new H1("Vaccination History");

        val authenticatedUser = securityService.getAuthenticatedUser();

        if (Objects.nonNull(authenticatedUser)) {

            searchField.setWidth("15%");
            searchField.setMinWidth("100px");
            searchField.setPlaceholder("Search");
            searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
            searchField.setValueChangeMode(ValueChangeMode.EAGER);
            searchField.setClearButtonVisible(true);

            grid.addColumn(PatientVaccine::getVaccineDate).setHeader("Date").setSortable(true).setWidth("170px").setFlexGrow(0);
            grid.addColumn(v -> v.getVaccine().getName()).setHeader("Vaccine").setSortable(true).setWidth("300px").setFlexGrow(0);
            grid.addColumn(PatientVaccine::getComments).setHeader("Observations");

            val dataView = grid.setItems(patientService.getVaccinesEntries(authenticatedUser.getUsername()));

            searchField.addValueChangeListener(e -> dataView.refreshAll());


            dataView.addFilter(patientVaccine -> {
                val searchTerm = searchField.getValue().trim();

                if (searchTerm.isEmpty())
                    return true;

                boolean matchesName = patientVaccine.getVaccine().getName().toLowerCase().contains(searchTerm);
                boolean matchesComments = patientVaccine.getComments().toLowerCase().contains(searchTerm);
                boolean matchesDate = patientVaccine.getVaccineDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toLowerCase().contains(searchTerm);

                return matchesName || matchesComments || matchesDate;
            });

            grid.setAllRowsVisible(true);
        }


        add(title, searchField, grid);
    }
}