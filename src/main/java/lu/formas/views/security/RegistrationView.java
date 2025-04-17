package lu.sormas.views.security;


import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.val;
import lu.sormas.services.UserService;
import lu.sormas.views.MainView;
import lu.sormas.views.common.SormasLogo;
import lu.sormas.views.security.forms.PatientRegistrationForm;

@Route(value = "register", layout = MainView.class)
@PageTitle("Register")
@AnonymousAllowed
public class RegistrationView extends VerticalLayout {

    private final PatientRegistrationForm form;

    public RegistrationView(UserService userService) {
        form = new PatientRegistrationForm(userService);

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