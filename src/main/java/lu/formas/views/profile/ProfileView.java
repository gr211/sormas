package lu.formas.views.profile;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import lombok.val;
import lu.formas.security.SecurityService;
import lu.formas.services.PatientService;
import lu.formas.views.MainView;
import lu.formas.views.profile.forms.PatientProfileForm;

@RolesAllowed({"USER"})
@Route(value = "profile", layout = MainView.class)
@PageTitle("Edit profile")
public class ProfileView extends VerticalLayout {

    private final PatientProfileForm form;

    public ProfileView(PatientService patientService, SecurityService securityService) {

        form = new PatientProfileForm(patientService, securityService);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setHorizontalComponentAlignment(Alignment.CENTER, form);

        addClassName("profile-view");

        add(new H1("Your profile"), addForm());
    }

    private VerticalLayout addForm() {
        val content = new VerticalLayout(form);
        content.addClassNames("register-view");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        return content;
    }
}

