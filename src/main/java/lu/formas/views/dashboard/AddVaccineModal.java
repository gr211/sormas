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
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import lombok.Getter;
import lombok.val;
import lu.formas.security.SecurityService;
import lu.formas.services.PatientService;
import lu.formas.services.VaccineService;
import lu.formas.services.model.VaccinesByMaturityValue;
import lu.formas.views.profile.forms.AddVaccineBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Getter
@Transactional
public class AddVaccineModal extends Dialog {
    private Select<VaccinesByMaturityValue> select = new Select<>();
    private DatePicker datePicker = new DatePicker("Day of vaccination");
    private TextArea comments = new TextArea("Comments", "Other relevant information");

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

        binder.forField(comments)
                .bind(AddVaccineBean::getComments, AddVaccineBean::setComments);

        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        setClassName("vaccine-modal");

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

                    val vaccine = select.getValue().getVaccine();
                    val date = datePicker.getValue();
                    val extraComments = comments.getValue();

                    patientService.addToVaccines(email, vaccine, date, extraComments);

                }

                select.clear();
                datePicker.clear();

                select.setInvalid(false);
                datePicker.setInvalid(false);

                close();
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

    public void prepareForm() {
        val vaccinesByMaturity = vaccineService.groupedByMaturity();

        select.setLabel("Select vaccine");
        select.setItems(vaccinesByMaturity.flatten());
        select.setPlaceholder("Select a vaccine");

        select.setItemEnabledProvider(
                item -> !item.isPlaceholder());

        select.setWidthFull();

        select.setRenderer(
                new ComponentRenderer<>(vaccine -> new Div(vaccine.getVaccine().getName()))
        );

        comments.addClassName("vaccine-comments");
    }
}