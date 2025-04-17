package lu.sormas.views.dashboard;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializablePredicate;
import lombok.val;
import lu.sormas.repository.model.PatientVaccine;
import lu.sormas.security.SecurityService;
import lu.sormas.services.PatientService;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;

public class VaccinationHistoryGrid extends VerticalLayout {

    private Grid<PatientVaccine> grid = new Grid<>(PatientVaccine.class, false);
    private TextField searchField = new TextField();

    private PatientService patientService;
    private SecurityService securityService;
    private VaccineNotifications vaccineNotifications;

    public VaccinationHistoryGrid(PatientService patientService, SecurityService securityService, VaccineNotifications vaccineNotifications) {
        this.patientService = patientService;
        this.securityService = securityService;
        this.vaccineNotifications = vaccineNotifications;

        setClassName("vaccination-history-grid-layout");
        setJustifyContentMode(JustifyContentMode.START);
        setAlignItems(Alignment.START);

        val authenticatedUser = securityService.getAuthenticatedUser();

        if (Objects.nonNull(authenticatedUser)) {
            prepareGrid(authenticatedUser.getUsername());
        }


        add(searchField, grid);
    }

    void prepareGrid(String email) {
        searchField.setClassName("vaccination-history-search-field");
        searchField.setPlaceholder("Search");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.setClearButtonVisible(true);

        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        grid.setClassName("vaccination-history-grid");

        grid.addColumn(
                new ComponentRenderer<>(Button::new, (button, patientVaccine) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> {
                        val modal = new ConfirmDeleteVaccineModal(this, patientService, patientVaccine, vaccineNotifications, email);
                        modal.open();
                    });
                    button.setIcon(new Icon(VaadinIcon.TRASH));
                })).setFlexGrow(0).setHeader("Manage");


        grid.addColumn(new LocalDateRenderer<>(PatientVaccine::getVaccineDate, () -> DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))).setWidth("5%").setFlexGrow(0).setHeader("Date");
        grid.addColumn(v -> v.getVaccine().getName()).setHeader("Vaccine").setSortable(true).setWidth("10%").setFlexGrow(0);
        grid.addColumn(commentsRenderer()).setHeader("Observations").setWidth("80%");

        val dataView = grid.setItems(patientService.getVaccinesEntries(email));

        searchField.addValueChangeListener(e -> dataView.refreshAll());

        dataView.addFilter(VaccinationHistoryGrid.dataviewFilter(searchField));

        grid.setAllRowsVisible(true);
    }

    private static SerializablePredicate<PatientVaccine> dataviewFilter(TextField searchField) {
        return patientVaccine -> {
            val searchTerm = searchField.getValue().trim().toLowerCase();

            if (searchTerm.isEmpty()) return true;

            boolean matchesName = patientVaccine.getVaccine().getName().toLowerCase().contains(searchTerm);
            boolean matchesComments = patientVaccine.getComments().toLowerCase().contains(searchTerm);
            boolean matchesDate = patientVaccine.getVaccineDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)).toLowerCase().contains(searchTerm);

            return matchesName || matchesComments || matchesDate;
        };
    }

    void refresh() {
        val authenticatedUser = securityService.getAuthenticatedUser();
        val dataView = grid.setItems(patientService.getVaccinesEntries(authenticatedUser.getUsername()));
        dataView.addFilter(VaccinationHistoryGrid.dataviewFilter(searchField));
    }

    private static Renderer<PatientVaccine> commentsRenderer() {
        return LitRenderer.<PatientVaccine>of(
                        "<i>${item.comments}</i>")
                .withProperty("comments", PatientVaccine::getComments);
    }
}