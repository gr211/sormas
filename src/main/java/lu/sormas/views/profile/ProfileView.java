package lu.sormas.views.profile;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import lombok.val;
import lu.sormas.security.SecurityService;
import lu.sormas.services.PatientService;
import lu.sormas.services.UserService;
import lu.sormas.views.MainView;
import lu.sormas.views.profile.forms.PatientProfileForm;

@RolesAllowed({"USER"})
@Route(value = "profile", layout = MainView.class)
@PageTitle("Edit profile")
public class ProfileView extends VerticalLayout {

    private final PatientProfileForm form;

    public ProfileView(UserService userService, SecurityService securityService, PatientService patientService) {

        form = new PatientProfileForm(userService, securityService, patientService);

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

