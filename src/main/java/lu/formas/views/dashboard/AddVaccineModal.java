package lu.formas.views.dashboard;

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
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import lombok.Getter;
import lombok.val;
import lu.formas.repository.model.PatientVaccine;
import lu.formas.security.SecurityService;
import lu.formas.services.PatientService;
import lu.formas.services.VaccineService;
import lu.formas.services.model.VaccinesByMaturityValue;
import lu.formas.views.profile.forms.AddVaccineBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

@Getter
@Transactional
public class AddVaccineModal extends Dialog {
    private Select<VaccinesByMaturityValue> select = new Select<>();
    private DatePicker datePicker = new DatePicker("Day of vaccination");

    private final VaccineService vaccineService;
    private final SecurityService securityService;

    private Binder<AddVaccineBean> binder = new BeanValidationBinder<>(AddVaccineBean.class);
    private AddVaccineBean bean = new AddVaccineBean();

    public AddVaccineModal(PatientService patientService, VaccineService vaccineService, SecurityService securityService) {
        this.vaccineService = vaccineService;
        this.securityService = securityService;

        binder.forField(select).asRequired().bind(AddVaccineBean::getSelect, AddVaccineBean::setSelect);
        binder.forField(datePicker)
                .asRequired()
                .bind(AddVaccineBean::getDatePicker, AddVaccineBean::setDatePicker);

        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        setClassName("vaccine-modal");

        setMinHeight("500px");
        setMinWidth("800px");

        prepareForm();

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

                val authenticatedUser = securityService.getAuthenticatedUser();
                if (Objects.nonNull(authenticatedUser)) {
                    val email = authenticatedUser.getUsername();
                    val patient = patientService.byEMail(email).get();
                    val vaccine = select.getValue().getVaccine();
                    val date = datePicker.getValue();

                    val patientVaccine = new PatientVaccine();
                    patientVaccine.setPatient(patient);
                    patientVaccine.setVaccine(vaccine);
                    patientVaccine.setVaccineDate(date);

                    val l = patient.getPatientVaccines();
                    l.add(patientVaccine);
                    patient.setPatientVaccines(l);
                    patientService.save(patient);
                }
                close();
            });
            addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            val closeButton = new Button("Close", event -> close());

            add(addButton, closeButton);
        }};

        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        modalLayout.add(select, buttonLayout);

        add(modalLayout);
    }

    public void prepareForm() {
        val vaccinesByMaturity = vaccineService.groupedByMaturity();

        select.setLabel("Select vaccine");
        select.setItems(vaccinesByMaturity.flatten());
        select.setPlaceholder("Select a vaccine");

        select.setItemEnabledProvider(
                item -> !item.isPlaceholder());

        select.setWidthFull();
        select.setHeight("80%");

        select.setRenderer(
                new ComponentRenderer<>(vaccine -> new Div(vaccine.getVaccine().getName()))
        );
    }
}