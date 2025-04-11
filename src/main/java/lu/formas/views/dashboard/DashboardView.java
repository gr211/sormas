package lu.formas.views.dashboard;


import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.RolesAllowed;
import lombok.val;
import lu.formas.security.SecurityService;
import lu.formas.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@Route("dashboard")
@RolesAllowed({"USER"})
public class DashboardView extends AppLayout {

    transient AuthenticationContext authContext;
    SecurityService securityService;

    private final PatientService personService;

    @Autowired
    public DashboardView(PatientService personService, AuthenticationContext authContext, SecurityService securityService) {
        this.authContext = authContext;
        this.securityService = securityService;
        this.personService = personService;

        val logo = new H1("Dashboard");
        logo.addClassName("logo");

        // Add welcome message with username
        String username = authContext.getPrincipalName().get();
        H2 h2 = new H2("Welcome, " + username + "!");

        if (Objects.nonNull(securityService.getAuthenticatedUser())) {
            Button logout = new Button("Logout", click -> securityService.logout());
            val header = new HorizontalLayout(logo, logout);
            header.setAlignItems(FlexComponent.Alignment.BASELINE);
            addToNavbar(header);
        }

    }
}