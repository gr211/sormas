package lu.formas.views.dashboard;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import lombok.Getter;
import lombok.val;
import lu.formas.services.PatientService;
import lu.formas.services.VaccineService;
import lu.formas.services.model.VaccinesByMaturityValue;

@Getter
public class AddVaccineModal extends Dialog {
    private Select<VaccinesByMaturityValue> select;
    private final VaccineService vaccineService;

    public AddVaccineModal(PatientService patientService, VaccineService vaccineService) {
        this.vaccineService = vaccineService;

        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        setClassName("confirm-delete");

        setMinHeight("500px");
        setMinWidth("800px");

        prepareForm();

        val modalLayout = new VerticalLayout();
        val message = new H2("Which vaccine would you like to add?");
        modalLayout.add(message);

        val datePicker = new DatePicker("Day of vaccination");
        modalLayout.add(datePicker);

        val buttonLayout = new HorizontalLayout() {{
            val addButton = new Button("Add", event -> {
                close();
            });
            addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            val closeButton = new Button("Close", event -> {
                close();
            });

            add(addButton, closeButton);
        }};

        modalLayout.add(select, buttonLayout);

        add(modalLayout);
    }

    public void prepareForm() {
        val vaccinesByMaturity = vaccineService.groupedByMaturity();

        select = new Select<>();
        select.setLabel("Select vaccine");
        select.setItems(vaccinesByMaturity.flatten());
        select.setPlaceholder("Select a vaccine");

        select.setItemEnabledProvider(
                item -> !item.isPlaceholder());

        select.setWidthFull();

        select.setRenderer(
                new ComponentRenderer<>(vaccine -> new Div(vaccine.getVaccine().getName()))
        );
    }
}