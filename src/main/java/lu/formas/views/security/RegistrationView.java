package lu.formas.views.security;


import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.val;
import lu.formas.security.SecurityService;
import lu.formas.services.PatientService;
import lu.formas.views.MainView;
import lu.formas.views.common.SormasLogo;
import lu.formas.views.security.forms.PatientRegistrationForm;

@Route(value = "register", layout = MainView.class)
@PageTitle("Register")
@AnonymousAllowed
public class RegistrationView extends VerticalLayout {

    private final PatientRegistrationForm form;

    public RegistrationView(PatientService patientService, SecurityService securityService) {
        form = new PatientRegistrationForm(patientService, securityService);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setHorizontalComponentAlignment(Alignment.CENTER, form);

        addClassName("register-view");

        configureForm();

        add(new SormasLogo(), new H1("Registration"), addForm());
    }

    private VerticalLayout addForm() {
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