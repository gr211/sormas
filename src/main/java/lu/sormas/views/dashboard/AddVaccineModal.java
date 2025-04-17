package lu.sormas.views.dashboard;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import lombok.Getter;
import lombok.val;
import lu.sormas.repository.model.Patient;
import lu.sormas.repository.model.PatientVaccine;
import lu.sormas.security.SecurityService;
import lu.sormas.services.PatientService;
import lu.sormas.services.VaccineService;
import lu.sormas.services.model.VaccinesByMaturityValue;
import lu.sormas.views.profile.forms.AddVaccineBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Getter
@Transactional
public class AddVaccineModal extends Dialog {
    private Select<VaccinesByMaturityValue> select = new Select<>();
    private DatePicker datePicker = new DatePicker("Day of vaccination");
    private TextArea comments = new TextArea("Comments", "Other relevant information");

    private final VaccineService vaccineService;
    private final SecurityService securityService;
    private final PatientService patientService;
    private final VaccineNotifications vaccineNotifications;

    private VaccinationHistoryGrid vaccinationHistoryGrid;

    private Binder<AddVaccineBean> binder = new BeanValidationBinder<>(AddVaccineBean.class);
    private AddVaccineBean bean = new AddVaccineBean();

    public AddVaccineModal(VaccinationHistoryGrid vaccinationHistoryGrid, PatientService patientService, VaccineService vaccineService, SecurityService securityService, VaccineNotifications vaccineNotifications) {
        this.vaccineService = vaccineService;
        this.securityService = securityService;
        this.patientService = patientService;
        this.vaccinationHistoryGrid = vaccinationHistoryGrid;
        this.vaccineNotifications = vaccineNotifications;

        val authenticatedUser = securityService.getAuthenticatedUser();

        binder.forField(select).asRequired().bind(AddVaccineBean::getSelect, AddVaccineBean::setSelect);
        binder.forField(datePicker)
                .asRequired()
                .bind(AddVaccineBean::getDatePicker, AddVaccineBean::setDatePicker);

        binder.forField(comments)
                .bind(AddVaccineBean::getComments, AddVaccineBean::setComments);

        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        setClassName("vaccine-modal");

        prepareForm(authenticatedUser.getUsername());

        val modalLayout = new VerticalLayout();
        val message = new H2("Which vaccine would you like to add?");
        modalLayout.add(message);
        modalLayout.setWidthFull();
        modalLayout.setHeightFull();

        datePicker.setRequired(true);
        modalLayout.add(datePicker);

        val buttonLayout = new HorizontalLayout() {{
            val addButton = new Button("Add", event -> {

                binder.writeBeanIfValid(bean);

                if (binder.isValid()) {
                    submit();
                    close();
                }
            });
            addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            val closeButton = new Button("Close", event -> close());

            add(addButton, closeButton);
        }};

        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        modalLayout.add(select, comments, buttonLayout);

        add(modalLayout);
    }

    public void submit() {
        val authenticatedUser = securityService.getAuthenticatedUser();
        val email = authenticatedUser.getUsername();

        val vaccine = select.getValue().getVaccine();
        val date = datePicker.getValue();
        val extraComments = comments.getValue();

        patientService.addToVaccines(email, vaccine, date, extraComments);

        select.clear();
        datePicker.clear();

        select.setInvalid(false);
        datePicker.setInvalid(false);

        vaccinationHistoryGrid.refresh();
        vaccineNotifications.refresh();
    }

    public void prepareForm(String email) {
        val vaccinesByMaturity = vaccineService.groupedByMaturity();

        select.setLabel("Select vaccine");
        select.setItems(vaccinesByMaturity.flatten());
        select.setPlaceholder("Select a vaccine");

        val vaccineList = patientService.getVaccinesEntries(email).stream().map(PatientVaccine::getVaccine).collect(Collectors.toList());

        select.setItemEnabledProvider(
                byMaturityValue -> !byMaturityValue.isPlaceholder() && !vaccineList.contains(byMaturityValue.getVaccine())
        );

        select.setWidthFull();

        select.setRenderer(
                new ComponentRenderer<>(vaccine -> new Div(vaccine.getVaccine().getName()))
        );

        comments.addClassName("vaccine-comments");
    }
}