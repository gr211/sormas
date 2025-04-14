package lu.formas.views.profile;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.RolesAllowed;
import lombok.val;
import lu.formas.security.SecurityService;
import lu.formas.services.PatientService;
import lu.formas.views.MainView;
import lu.formas.views.security.forms.PatientRegistrationForm;

@RolesAllowed({"USER"})
@Tag("div")
@Route(value = "profile", layout = MainView.class)
public class ProfileView extends VerticalLayout {

    transient AuthenticationContext authContext;
    SecurityService securityService;

    private final PatientRegistrationForm form;

    public ProfileView(PatientService patientService, AuthenticationContext authContext, SecurityService securityService) {

        this.form = new PatientRegistrationForm(patientService);

        add(addForm());
    }

    private VerticalLayout addForm() {
        val content = new VerticalLayout(form);
        content.setWidthFull();

        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        content.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, form);

        return content;
    }
}