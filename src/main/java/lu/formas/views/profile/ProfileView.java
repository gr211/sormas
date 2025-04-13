package lu.formas.views.profile;


import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.val;
import lu.formas.services.PatientService;
import lu.formas.views.security.forms.PatientRegistrationForm;

@Route("profile")
@PageTitle("Profile")
@AnonymousAllowed
public class ProfileView extends VerticalLayout {

    private final PatientRegistrationForm form;

    public ProfileView(PatientService patientService) {
        this.form = new PatientRegistrationForm(patientService);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setHorizontalComponentAlignment(Alignment.CENTER, form);

        addClassName("list-view");
        setSizeFull();

        configureForm();

        add(new H1("Edit profile"), getContent());
    }

    private VerticalLayout getContent() {
        val content = new VerticalLayout(form);
        content.addClassNames("register-view");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        return content;
    }

    private void configureForm() {
        form.setVisible(true);
    }
}