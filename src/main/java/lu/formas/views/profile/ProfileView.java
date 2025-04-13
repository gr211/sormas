package lu.formas.views.profile;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import lombok.val;
import lu.formas.security.SecurityService;
import lu.formas.services.PatientService;
import lu.formas.views.MainView;
import lu.formas.views.security.forms.PatientRegistrationForm;

@Route("profile")
@PageTitle("Profile")
@AnonymousAllowed
public class ProfileView extends MainView {

    transient AuthenticationContext authContext;
    SecurityService securityService;

    private final PatientRegistrationForm form;

    public ProfileView(PatientService patientService, AuthenticationContext authContext, SecurityService securityService) {
        super(patientService, authContext, securityService);

        this.form = new PatientRegistrationForm(patientService);

        setContent(addForm());
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