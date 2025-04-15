package lu.formas.views.dashboard;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import lombok.val;
import lu.formas.repository.VaccinRepository;
import lu.formas.repository.model.Patient;
import lu.formas.repository.model.Vaccine;
import lu.formas.security.SecurityService;
import lu.formas.services.PatientService;
import lu.formas.services.VaccineService;

public class AddVaccineModal extends Dialog {
    private Select<Vaccine> select;
    private final VaccineService vaccineService;

    public AddVaccineModal(PatientService patientService, VaccineService vaccineService) {
        this.vaccineService = vaccineService;

        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        setClassName("confirm-delete");

        setWidth("1200px");
        setHeight("1200px");

        prepareForm();

        val dialogLayout = new VerticalLayout();
        val message = new H2("Add vaccination");
        dialogLayout.add(message);

        val buttonsLayout = new HorizontalLayout();
        val confirmButton = new Button("Confirm", event -> {
            Notification.show("Operation approved");
            close();
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        val cancelButton = new Button("Cancel", event -> {
            Notification.show("Operation cancelled");
            close();
        });
        buttonsLayout.add(confirmButton, cancelButton);


        dialogLayout.add(buttonsLayout);
        add(dialogLayout, select);
    }

    void prepareForm() {
        val li = vaccineService.vaccines();

        select = new Select<>();
        select.setLabel("Select vaccine");
        select.setItems(li);
        select.setPlaceholder("Select a vaccine");

        select.addComponents(li.get(5), new Hr());

        select.setWidth("300px");
        select.setHeight("300px");

        select.setRenderer(
                new ComponentRenderer<>(vaccine -> {
                    return new Div(vaccine.getName());
                })

        );

        select.setWidthFull();
    }
}